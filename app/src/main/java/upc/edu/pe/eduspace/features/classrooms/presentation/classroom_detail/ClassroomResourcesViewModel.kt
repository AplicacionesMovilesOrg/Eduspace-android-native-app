package upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.classrooms.domain.models.Resource
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.CreateResource
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.ResourcesRepository
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.UpdateResource
import javax.inject.Inject

@HiltViewModel
class ClassroomResourcesViewModel @Inject constructor(
    private val resourcesRepository: ResourcesRepository
) : ViewModel() {

    private val _resourcesState = MutableStateFlow<UiState<List<Resource>>>(UiState.Initial)
    val resourcesState: StateFlow<UiState<List<Resource>>> = _resourcesState.asStateFlow()

    private val _createResourceState = MutableStateFlow<UiState<Resource>>(UiState.Initial)
    val createResourceState: StateFlow<UiState<Resource>> = _createResourceState.asStateFlow()

    private val _updateResourceState = MutableStateFlow<UiState<Resource>>(UiState.Initial)
    val updateResourceState: StateFlow<UiState<Resource>> = _updateResourceState.asStateFlow()

    private val _deleteResourceState = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val deleteResourceState: StateFlow<UiState<Boolean>> = _deleteResourceState.asStateFlow()

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
}
