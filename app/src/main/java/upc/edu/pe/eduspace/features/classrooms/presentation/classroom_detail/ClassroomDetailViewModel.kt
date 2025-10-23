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
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.ClassroomsRepository
import upc.edu.pe.eduspace.features.meetings.domain.models.CreateMeeting
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.repositories.MeetingsRepository
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.TeachersRepository
import javax.inject.Inject

@HiltViewModel
class ClassroomDetailViewModel @Inject constructor(
    private val classroomsRepository: ClassroomsRepository,
    private val teachersRepository: TeachersRepository,
    private val meetingsRepository: MeetingsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _classroomState = MutableStateFlow<UiState<Classroom>>(UiState.Initial)
    val classroomState: StateFlow<UiState<Classroom>> = _classroomState.asStateFlow()

    private val _teacherState = MutableStateFlow<UiState<Teacher>>(UiState.Initial)
    val teacherState: StateFlow<UiState<Teacher>> = _teacherState.asStateFlow()

    private val _createMeetingState = MutableStateFlow<UiState<Meeting>>(UiState.Initial)
    val createMeetingState: StateFlow<UiState<Meeting>> = _createMeetingState.asStateFlow()

    fun getClassroomById(classroomId: String) {
        viewModelScope.launch {
            _classroomState.value = UiState.Loading
            try {
                val classroom = classroomsRepository.getClassroomById(classroomId)
                if (classroom != null) {
                    _classroomState.value = UiState.Success(classroom)
                    // Load teacher sequentially in the same coroutine (no race condition)
                    loadTeacherById(classroom.teacherId)
                } else {
                    _classroomState.value = UiState.Error("Classroom not found")
                }
            } catch (e: Exception) {
                _classroomState.value = UiState.Error(e.message ?: "Error getting classroom")
            }
        }
    }

    private suspend fun loadTeacherById(teacherId: String) {
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

    fun createMeeting(
        classroomId: String,
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
