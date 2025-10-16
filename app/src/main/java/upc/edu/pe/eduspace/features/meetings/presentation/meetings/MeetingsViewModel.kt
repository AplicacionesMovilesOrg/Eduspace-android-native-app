package upc.edu.pe.eduspace.features.meetings.presentation.meetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.repositories.MeetingsRepository
import javax.inject.Inject

@HiltViewModel
class MeetingsViewModel @Inject constructor(
    private val repository: MeetingsRepository
) : ViewModel() {

    private val _meetingsState = MutableStateFlow<UiState<List<Meeting>>>(UiState.Initial)
    val meetingsState: StateFlow<UiState<List<Meeting>>> = _meetingsState.asStateFlow()

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
                _meetingsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
