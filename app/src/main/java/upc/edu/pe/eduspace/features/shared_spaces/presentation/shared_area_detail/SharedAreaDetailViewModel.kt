package upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_area_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedSpaceType
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.UpdateSharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.repositories.SharedAreasRepository
import javax.inject.Inject

@HiltViewModel
class SharedAreaDetailViewModel @Inject constructor(
    private val repository: SharedAreasRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sharedAreaId: Int = savedStateHandle.get<Int>("sharedAreaId") ?: 0

    private val _sharedAreaState = MutableStateFlow<UiState<SharedArea>>(UiState.Initial)
    val sharedAreaState: StateFlow<UiState<SharedArea>> = _sharedAreaState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<SharedArea>>(UiState.Initial)
    val updateState: StateFlow<UiState<SharedArea>> = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val deleteState: StateFlow<UiState<Boolean>> = _deleteState.asStateFlow()

    init {
        loadSharedArea()
    }

    fun loadSharedArea() {
        viewModelScope.launch {
            _sharedAreaState.value = UiState.Loading
            try {
                val sharedArea = repository.getSharedAreaById(sharedAreaId)
                if (sharedArea != null) {
                    _sharedAreaState.value = UiState.Success(sharedArea)
                } else {
                    _sharedAreaState.value = UiState.Error("Shared area not found")
                }
            } catch (e: Exception) {
                _sharedAreaState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateSharedArea(type: SharedSpaceType, capacity: Int, description: String) {
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            try {
                val sharedArea = UpdateSharedArea(sharedAreaId, type, capacity, description)
                val result = repository.updateSharedArea(sharedArea)
                if (result != null) {
                    _updateState.value = UiState.Success(result)
                    loadSharedArea() // Reload details
                } else {
                    _updateState.value = UiState.Error("Error updating shared area")
                }
            } catch (e: Exception) {
                _updateState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteSharedArea() {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            try {
                val result = repository.deleteSharedArea(sharedAreaId)
                if (result) {
                    _deleteState.value = UiState.Success(true)
                } else {
                    _deleteState.value = UiState.Error("Error deleting shared area")
                }
            } catch (e: Exception) {
                _deleteState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = UiState.Initial
    }

    fun resetDeleteState() {
        _deleteState.value = UiState.Initial
    }
}
