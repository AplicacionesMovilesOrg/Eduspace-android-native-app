package upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.CreateSharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedSpaceType
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.UpdateSharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.repositories.SharedAreasRepository
import javax.inject.Inject

@HiltViewModel
class SharedAreasViewModel @Inject constructor(
    private val repository: SharedAreasRepository
) : ViewModel() {

    private val _sharedAreasState = MutableStateFlow<UiState<List<SharedArea>>>(UiState.Initial)
    val sharedAreasState: StateFlow<UiState<List<SharedArea>>> = _sharedAreasState.asStateFlow()

    private val _createState = MutableStateFlow<UiState<SharedArea>>(UiState.Initial)
    val createState: StateFlow<UiState<SharedArea>> = _createState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<SharedArea>>(UiState.Initial)
    val updateState: StateFlow<UiState<SharedArea>> = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val deleteState: StateFlow<UiState<Boolean>> = _deleteState.asStateFlow()

    init {
        loadSharedAreas()
    }

    fun loadSharedAreas() {
        viewModelScope.launch {
            _sharedAreasState.value = UiState.Loading
            try {
                val sharedAreas = repository.getAllSharedAreas()
                _sharedAreasState.value = UiState.Success(sharedAreas)
            } catch (e: Exception) {
                _sharedAreasState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createSharedArea(type: SharedSpaceType, capacity: Int, description: String) {
        viewModelScope.launch {
            _createState.value = UiState.Loading
            try {
                val sharedArea = CreateSharedArea(type, capacity, description)
                val result = repository.createSharedArea(sharedArea)
                if (result != null) {
                    _createState.value = UiState.Success(result)
                    loadSharedAreas() // Reload list
                } else {
                    _createState.value = UiState.Error("Error creating shared area")
                }
            } catch (e: Exception) {
                _createState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateSharedArea(id: String, type: SharedSpaceType, capacity: Int, description: String) {
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            try {
                val sharedArea = UpdateSharedArea(id, type, capacity, description)
                val result = repository.updateSharedArea(sharedArea)
                if (result != null) {
                    _updateState.value = UiState.Success(result)
                    loadSharedAreas() // Reload list
                } else {
                    _updateState.value = UiState.Error("Error updating shared area")
                }
            } catch (e: Exception) {
                _updateState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteSharedArea(id: String) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            try {
                val result = repository.deleteSharedArea(id)
                if (result) {
                    _deleteState.value = UiState.Success(true)
                    loadSharedAreas() // Reload list
                } else {
                    _deleteState.value = UiState.Error("Error deleting shared area")
                }
            } catch (e: Exception) {
                _deleteState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetCreateState() {
        _createState.value = UiState.Initial
    }

    fun resetUpdateState() {
        _updateState.value = UiState.Initial
    }

    fun resetDeleteState() {
        _deleteState.value = UiState.Initial
    }
}
