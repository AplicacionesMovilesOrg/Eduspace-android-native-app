package upc.edu.pe.eduspace.features.meetings.presentation.meeting_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.models.UpdateMeeting
import upc.edu.pe.eduspace.features.meetings.domain.repositories.MeetingsRepository
import javax.inject.Inject

@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    private val repository: MeetingsRepository,
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

    init {
        loadMeeting()
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
                    loadMeeting() // Reload to get updated teachers list
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
