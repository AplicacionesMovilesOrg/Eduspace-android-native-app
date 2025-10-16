package upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.data.SessionManager
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom
import upc.edu.pe.eduspace.features.classrooms.domain.models.Resource
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.ClassroomsRepository
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.CreateResource
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.ResourcesRepository
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.UpdateResource
import upc.edu.pe.eduspace.features.meetings.domain.models.CreateMeeting
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.repositories.MeetingsRepository
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.TeachersRepository
import javax.inject.Inject

@HiltViewModel
class ClassroomDetailViewModel @Inject constructor(
    private val classroomsRepository: ClassroomsRepository,
    private val resourcesRepository: ResourcesRepository,
    private val teachersRepository: TeachersRepository,
    private val meetingsRepository: MeetingsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _classroomState = MutableStateFlow<UiState<Classroom>>(UiState.Initial)
    val classroomState: StateFlow<UiState<Classroom>> = _classroomState.asStateFlow()

    private val _teacherState = MutableStateFlow<UiState<Teacher>>(UiState.Initial)
    val teacherState: StateFlow<UiState<Teacher>> = _teacherState.asStateFlow()

    private val _resourcesState = MutableStateFlow<UiState<List<Resource>>>(UiState.Initial)
    val resourcesState: StateFlow<UiState<List<Resource>>> = _resourcesState.asStateFlow()

    private val _createResourceState = MutableStateFlow<UiState<Resource>>(UiState.Initial)
    val createResourceState: StateFlow<UiState<Resource>> = _createResourceState.asStateFlow()

    private val _updateResourceState = MutableStateFlow<UiState<Resource>>(UiState.Initial)
    val updateResourceState: StateFlow<UiState<Resource>> = _updateResourceState.asStateFlow()

    private val _deleteResourceState = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val deleteResourceState: StateFlow<UiState<Boolean>> = _deleteResourceState.asStateFlow()

    private val _createMeetingState = MutableStateFlow<UiState<Meeting>>(UiState.Initial)
    val createMeetingState: StateFlow<UiState<Meeting>> = _createMeetingState.asStateFlow()

    fun getClassroomById(classroomId: Int) {
        viewModelScope.launch {
            _classroomState.value = UiState.Loading
            try {
                val classroom = classroomsRepository.getClassroomById(classroomId)
                if (classroom != null) {
                    _classroomState.value = UiState.Success(classroom)
                    getTeacherById(classroom.teacherId)
                } else {
                    _classroomState.value = UiState.Error("Classroom not found")
                }
            } catch (e: Exception) {
                _classroomState.value = UiState.Error(e.message ?: "Error getting classroom")
            }
        }
    }

    fun getTeacherById(teacherId: Int) {
        viewModelScope.launch {
            _teacherState.value = UiState.Loading
            try {
                val teacher = teachersRepository.getTeacherById(teacherId)
                if (teacher != null) {
                    _teacherState.value = UiState.Success(teacher)
                } else {
                    _teacherState.value = UiState.Error("Teacher not found")
                }
            } catch (e: Exception) {
                _teacherState.value = UiState.Error(e.message ?: "Error getting teacher")
            }
        }
    }

    fun getResourcesByClassroomId(classroomId: Int) {
        viewModelScope.launch {
            _resourcesState.value = UiState.Loading
            try {
                val resources = resourcesRepository.getResourcesByClassroomId(classroomId)
                _resourcesState.value = UiState.Success(resources)
            } catch (e: Exception) {
                _resourcesState.value = UiState.Error(e.message ?: "Error getting resources")
            }
        }
    }

    fun createResource(classroomId: Int, name: String, kindOfResource: String) {
        viewModelScope.launch {
            _createResourceState.value = UiState.Loading
            try {
                val input = CreateResource(
                    name = name,
                    kindOfResource = kindOfResource
                )
                val resource = resourcesRepository.createResource(classroomId, input)
                if (resource != null) {
                    _createResourceState.value = UiState.Success(resource)
                    getResourcesByClassroomId(classroomId)
                } else {
                    _createResourceState.value = UiState.Error("Failed to create resource. Please try again.")
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("already exists") == true -> e.message!!
                    else -> "Error creating resource: ${e.message ?: "Unknown error"}"
                }
                _createResourceState.value = UiState.Error(errorMessage)
            }
        }
    }

    fun updateResource(classroomId: Int, resourceId: Int, name: String, kindOfResource: String) {
        viewModelScope.launch {
            _updateResourceState.value = UiState.Loading
            try {
                val input = UpdateResource(
                    id = resourceId,
                    name = name,
                    kindOfResource = kindOfResource,
                    classroomId = classroomId
                )
                val resource = resourcesRepository.updateResource(classroomId, resourceId, input)
                if (resource != null) {
                    _updateResourceState.value = UiState.Success(resource)
                    getResourcesByClassroomId(classroomId)
                } else {
                    _updateResourceState.value = UiState.Error("Error updating resource")
                }
            } catch (e: Exception) {
                _updateResourceState.value = UiState.Error(e.message ?: "Error updating resource")
            }
        }
    }

    fun deleteResource(classroomId: Int, resourceId: Int) {
        viewModelScope.launch {
            _deleteResourceState.value = UiState.Loading
            try {
                val success = resourcesRepository.deleteResource(classroomId, resourceId)
                if (success) {
                    _deleteResourceState.value = UiState.Success(true)
                    getResourcesByClassroomId(classroomId)
                } else {
                    _deleteResourceState.value = UiState.Error("Error deleting resource")
                }
            } catch (e: Exception) {
                _deleteResourceState.value = UiState.Error(e.message ?: "Error deleting resource")
            }
        }
    }

    fun resetCreateResourceState() {
        _createResourceState.value = UiState.Initial
    }

    fun resetUpdateResourceState() {
        _updateResourceState.value = UiState.Initial
    }

    fun resetDeleteResourceState() {
        _deleteResourceState.value = UiState.Initial
    }

    fun createMeeting(
        classroomId: Int,
        title: String,
        description: String,
        date: String,
        start: String,
        end: String
    ) {
        viewModelScope.launch {
            _createMeetingState.value = UiState.Loading
            try {
                val administratorId = sessionManager.adminIdFlow.firstOrNull()

                if (administratorId == null) {
                    _createMeetingState.value = UiState.Error("User not authenticated")
                    return@launch
                }

                val meeting = CreateMeeting(
                    title = title,
                    description = description,
                    date = date,
                    start = start,
                    end = end
                )
                val result = meetingsRepository.createMeeting(administratorId, classroomId, meeting)
                if (result != null) {
                    _createMeetingState.value = UiState.Success(result)
                } else {
                    _createMeetingState.value = UiState.Error("Error creating meeting")
                }
            } catch (e: Exception) {
                _createMeetingState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetCreateMeetingState() {
        _createMeetingState.value = UiState.Initial
    }
}
