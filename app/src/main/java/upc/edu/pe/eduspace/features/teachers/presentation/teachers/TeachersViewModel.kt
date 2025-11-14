package upc.edu.pe.eduspace.features.teachers.presentation.teachers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.CreateTeacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.TeachersRepository
import upc.edu.pe.eduspace.features.teachers.domain.repositories.UpdateTeacher
import javax.inject.Inject

@HiltViewModel
class TeachersViewModel @Inject constructor(
    private val repository: TeachersRepository
) : ViewModel() {

    private val _teachers = MutableStateFlow<UiState<List<Teacher>>>(UiState.Initial)
    val teachers: StateFlow<UiState<List<Teacher>>> = _teachers.asStateFlow()

    private val _createState = MutableStateFlow<UiState<Teacher>>(UiState.Initial)
    val createState: StateFlow<UiState<Teacher>> = _createState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val updateState: StateFlow<UiState<Unit>> = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val deleteState: StateFlow<UiState<Unit>> = _deleteState.asStateFlow()

    fun getAllTeachers() {
        viewModelScope.launch {
            _teachers.value = UiState.Loading
            try {
                val teachersList = repository.getAllTeachers()
                _teachers.value = UiState.Success(teachersList)
            } catch (e: Exception) {
                _teachers.value = UiState.Error(e.message ?: "Error loading teachers")
            }
        }
    }

    fun createTeacher(input: CreateTeacher) {
        viewModelScope.launch {
            _createState.value = UiState.Loading
            try {
                val created = repository.createTeacher(input)
                if (created != null) {
                    _createState.value = UiState.Success(created)
                    // Reload teachers list after successful creation
                    getAllTeachers()
                } else {
                    _createState.value = UiState.Error("Could not create teacher")
                }
            } catch (e: Exception) {
                _createState.value = UiState.Error(e.message ?: "Error creating teacher")
            }
        }
    }

    fun updateTeacher(id: String, input: UpdateTeacher) {
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            try {
                repository.updateTeacher(id, input)
                _updateState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _updateState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteTeacher(id: String) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            try {
                repository.deleteTeacher(id)
                _deleteState.value = UiState.Success(Unit)
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

    fun resetCreateState() {
        _createState.value = UiState.Initial
    }

    init {
        getAllTeachers()
    }
}