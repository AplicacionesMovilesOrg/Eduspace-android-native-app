package upc.edu.pe.eduspace.features.meetings.presentation.meeting_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.models.UpdateMeeting
import upc.edu.pe.eduspace.features.meetings.domain.repositories.MeetingsRepository
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.TeachersRepository
import javax.inject.Inject

@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    private val repository: MeetingsRepository,
    private val teachersRepository: TeachersRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val meetingId: Int = savedStateHandle.get<Int>("meetingId") ?: 0

    private val _meetingState = MutableStateFlow<UiState<Meeting>>(UiState.Initial)
    val meetingState: StateFlow<UiState<Meeting>> = _meetingState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<Meeting>>(UiState.Initial)
    val updateState: StateFlow<UiState<Meeting>> = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val deleteState: StateFlow<UiState<Boolean>> = _deleteState.asStateFlow()

    private val _addTeacherState = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val addTeacherState: StateFlow<UiState<Boolean>> = _addTeacherState.asStateFlow()

    private val _teachers = MutableStateFlow<UiState<List<Teacher>>>(UiState.Initial)
    val teachers: StateFlow<UiState<List<Teacher>>> = _teachers.asStateFlow()

    /**
     * A StateFlow that combines the list of all teachers and the meeting's current participants
     * to expose publicly only the teachers that have NOT been added to the meeting yet.
     */
    val availableTeachers: StateFlow<UiState<List<Teacher>>> =
        _meetingState.combine(_teachers) { meetingState, teachersState ->
            // Check that both flows have loaded successfully
            if (meetingState is UiState.Success && teachersState is UiState.Success) {
                val allTeachers = teachersState.data
                // Get the IDs of teachers already in the meeting
                val participatingTeacherIds = meetingState.data.teachers.map { it.id }.toSet()
                // Filter the complete list
                val filteredList = allTeachers.filter { it.id !in participatingTeacherIds }
                UiState.Success(filteredList)
            } else if (meetingState is UiState.Loading || teachersState is UiState.Loading) {
                UiState.Loading
            } else if (meetingState is UiState.Error) {
                UiState.Error(meetingState.message) // Propagate meeting error
            } else if (teachersState is UiState.Error) {
                UiState.Error(teachersState.message) // Propagate teachers error
            } else {
                UiState.Initial
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    init {
        loadMeeting()
        loadTeachers()
    }

    fun loadTeachers() {
        viewModelScope.launch {
            _teachers.value = UiState.Loading
            try {
                val teachersList = teachersRepository.getAllTeachers()
                _teachers.value = UiState.Success(teachersList)
            } catch (e: Exception) {
                _teachers.value = UiState.Error(e.message ?: "Unknown error loading teachers")
            }
        }
    }

    fun loadMeeting() {
        viewModelScope.launch {
            _meetingState.value = UiState.Loading
            try {
                val meeting = repository.getMeetingById(meetingId)
                if (meeting != null) {
                    _meetingState.value = UiState.Success(meeting)
                } else {
                    _meetingState.value = UiState.Error("Meeting not found")
                }
            } catch (e: Exception) {
                _meetingState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateMeeting(
        title: String,
        description: String,
        date: String,
        start: String,
        end: String
    ) {
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            try {
                val currentMeeting = (_meetingState.value as? UiState.Success)?.data
                if (currentMeeting != null) {
                    val updateMeeting = UpdateMeeting(
                        meetingId = currentMeeting.meetingId,
                        title = title,
                        description = description,
                        date = date,
                        start = start,
                        end = end,
                        administratorId = currentMeeting.administratorId,
                        classroomId = currentMeeting.classroomId
                    )
                    val result = repository.updateMeeting(updateMeeting)
                    if (result != null) {
                        _updateState.value = UiState.Success(result)
                        loadMeeting() // Reload to get updated data
                    } else {
                        _updateState.value = UiState.Error("Error updating meeting")
                    }
                } else {
                    _updateState.value = UiState.Error("Meeting data not available")
                }
            } catch (e: Exception) {
                _updateState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteMeeting() {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            try {
                val result = repository.deleteMeeting(meetingId)
                if (result) {
                    _deleteState.value = UiState.Success(true)
                } else {
                    _deleteState.value = UiState.Error("Error deleting meeting")
                }
            } catch (e: Exception) {
                _deleteState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun addTeacherToMeeting(teacherId: Int) {
        viewModelScope.launch {
            _addTeacherState.value = UiState.Loading
            try {
                val result = repository.addTeacherToMeeting(meetingId, teacherId)
                if (result) {
                    _addTeacherState.value = UiState.Success(true)

                    // --- FIX: Update local state to reflect the new participant ---
                    val allTeachersState = _teachers.value
                    val currentMeetingState = _meetingState.value

                    // Ensure both states are successful
                    if (allTeachersState is UiState.Success && currentMeetingState is UiState.Success) {
                        // 1. Find the Teacher object we just added
                        val teacherToAdd = allTeachersState.data.find { it.id == teacherId }

                        if (teacherToAdd != null) {
                            // 2. Convert Teacher to TeacherInfo (Meeting uses TeacherInfo)
                            val teacherInfo = upc.edu.pe.eduspace.features.meetings.domain.models.TeacherInfo(
                                id = teacherToAdd.id,
                                firstName = teacherToAdd.firstName,
                                lastName = teacherToAdd.lastName
                            )

                            // 3. Get current participants list and add the new one
                            val currentMeeting = currentMeetingState.data
                            val updatedTeachers = currentMeeting.teachers + teacherInfo

                            // 4. Emit the updated meeting state
                            _meetingState.value = UiState.Success(
                                currentMeeting.copy(teachers = updatedTeachers)
                            )
                        }
                    }
                    // --- END OF FIX ---
                } else {
                    _addTeacherState.value = UiState.Error("Error adding teacher to meeting")
                }
            } catch (e: Exception) {
                _addTeacherState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = UiState.Initial
    }

    fun resetDeleteState() {
        _deleteState.value = UiState.Initial
    }

    fun resetAddTeacherState() {
        _addTeacherState.value = UiState.Initial
    }
}
