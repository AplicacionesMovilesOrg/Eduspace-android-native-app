// kotlin
package upc.edu.pe.eduspace.features.meetings.presentation.meetings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.ClassroomsRepository
import upc.edu.pe.eduspace.features.home.domain.repositories.HomeRepository
import upc.edu.pe.eduspace.features.meetings.domain.models.CreateMeeting
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.repositories.MeetingsRepository
import javax.inject.Inject

@HiltViewModel
class MeetingsViewModel @Inject constructor(
    private val repository: MeetingsRepository,
    private val homeRepository: HomeRepository,
    private val classroomRepository: ClassroomsRepository
) : ViewModel() {

    private val _meetingsState = MutableStateFlow<UiState<List<Meeting>>>(UiState.Initial)
    val meetingsState: StateFlow<UiState<List<Meeting>>> = _meetingsState.asStateFlow()

    private val _classroomsState = MutableStateFlow<UiState<List<Classroom>>>(UiState.Initial)

    val classroomsState: StateFlow<UiState<List<Classroom>>> = _classroomsState.asStateFlow()
    private val _createState = MutableStateFlow<UiState<Meeting>>(UiState.Initial)
    val createState: StateFlow<UiState<Meeting>> = _createState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<Meeting>>(UiState.Initial)
    val updateState: StateFlow<UiState<Meeting>> = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val deleteState: StateFlow<UiState<Unit>> = _deleteState.asStateFlow()

    init {
        loadMeetings()
    }

    fun loadMeetings() {
        viewModelScope.launch {
            _meetingsState.value = UiState.Loading
            try {
                val meetings = repository.getAllMeetings()
                _meetingsState.value = UiState.Success(meetings)
            } catch (e: Exception) {
                _meetingsState.value = UiState.Error(e.message ?: "Error getting meetings")
            }
        }
    }

    fun resetCreateState() {
        _createState.value = UiState.Initial
    }

    private suspend fun fetchAdminId(): String {
        return try {
            val userHome = homeRepository.getAdministratorProfiles()
            userHome.id.also { Log.d("MeetingsVM", "fetchAdminId -> $it") }
        } catch (e: Exception) {
            Log.e("MeetingsVM", "fetchAdminId error", e)
            ""
        }
    }

    private suspend fun fetchClassroomId(): String {
        return try {
            val classrooms = classroomRepository.getAllClassrooms()
            val id = classrooms.firstOrNull()?.id ?: ""
            Log.d("MeetingsVM", "fetchClassroomId -> $id")
            id
        } catch (e: Exception) {
            Log.e("MeetingsVM", "fetchClassroomId error", e)
            ""
        }
    }

    private suspend fun getClassroomById(id: String): Classroom? {
        return try {
            val classroom = classroomRepository.getClassroomById(id)
            Log.d("MeetingsVM", "getClassroomById -> $classroom")
            classroom
        } catch (e: Exception) {
            Log.e("MeetingsVM", "getClassroomById error", e)
        } as Classroom?
    }


    fun createMeeting(
        classroomId: String,
        title: String,
        description: String,
        date: String,
        start: String,
        end: String,
    ) {
        viewModelScope.launch {
            _createState.value = UiState.Loading
            try {
                val adminId = fetchAdminId()
                Log.d("MeetingsVM", "createMeeting adminId='$adminId'")
                if (adminId.isBlank()) {
                    _createState.value =
                        UiState.Error("No se pudo obtener el id del administrador. Revisa logs y que el servicio de perfiles devuelva datos.")
                    return@launch
                }

//                val classroomId = fetchClassroomId()
//                Log.d("MeetingsVM", "createMeeting classroomId='$classroomId'")
//                if (classroomId.isBlank()) {
//                    _createState.value =
//                        UiState.Error("No se pudo obtener el id del aula. Revisa logs y que el servicio de aulas devuelva datos.")
//                    return@launch
//                }

                val meeting = CreateMeeting(classroomId, title, description, date, start, end)
                val result = repository.createMeeting(adminId, classroomId, meeting)
                if (result != null) {
                    _createState.value = UiState.Success(result)
                    loadMeetings()
                } else {
                    Log.e("MeetingsVM", "createMeeting: repository returned null")
                    _createState.value =
                        UiState.Error("No se creó la reunión — revisa logs del repositorio")
                }
            } catch (e: Exception) {
                Log.e("MeetingsVM", "createMeeting exception", e)
                _createState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getAllClassrooms() {
        viewModelScope.launch {
            _classroomsState.value = UiState.Loading
            try {
                val classrooms = classroomRepository.getAllClassrooms()
                _classroomsState.value = UiState.Success(classrooms)
            } catch (e: Exception) {
                _classroomsState.value = UiState.Error(e.message ?: "Error getting classrooms")
            }
        }
    }
}
