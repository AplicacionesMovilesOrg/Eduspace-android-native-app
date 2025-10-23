package upc.edu.pe.eduspace.features.classrooms.presentation.classrooms

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
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.CreateClassroom
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.UpdateClassroom
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.TeachersRepository
import javax.inject.Inject

@HiltViewModel
class ClassroomsViewModel @Inject constructor(
    private val repository: ClassroomsRepository,
    private val teachersRepository: TeachersRepository
) : ViewModel() {

    private val _classroomsState = MutableStateFlow<UiState<List<Classroom>>>(UiState.Initial)
    val classroomsState: StateFlow<UiState<List<Classroom>>> = _classroomsState.asStateFlow()

    private val _teachersState = MutableStateFlow<UiState<List<Teacher>>>(UiState.Initial)
    val teachersState: StateFlow<UiState<List<Teacher>>> = _teachersState.asStateFlow()

    private val _createState = MutableStateFlow<UiState<Classroom>>(UiState.Initial)
    val createState: StateFlow<UiState<Classroom>> = _createState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<Classroom>>(UiState.Initial)
    val updateState: StateFlow<UiState<Classroom>> = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Boolean>>(UiState.Initial)
    val deleteState: StateFlow<UiState<Boolean>> = _deleteState.asStateFlow()

    fun getAllClassrooms() {
        viewModelScope.launch {
            _classroomsState.value = UiState.Loading
            try {
                val classrooms = repository.getAllClassrooms()
                _classroomsState.value = UiState.Success(classrooms)
            } catch (e: Exception) {
                _classroomsState.value = UiState.Error(e.message ?: "Error getting classrooms")
            }
        }
    }

    fun getClassroomsByTeacherId(teacherId: String) {
        viewModelScope.launch {
            _classroomsState.value = UiState.Loading
            try {
                val classrooms = repository.getClassroomsByTeacherId(teacherId)
                _classroomsState.value = UiState.Success(classrooms)
            } catch (e: Exception) {
                _classroomsState.value = UiState.Error(e.message ?: "Error getting classrooms by teacher")
            }
        }
    }

    fun createClassroom(teacherId: String, name: String, description: String) {
        viewModelScope.launch {
            _createState.value = UiState.Loading
            try {
                val input = CreateClassroom(
                    teacherId = teacherId,
                    name = name,
                    description = description
                )
                val classroom = repository.createClassroom(input)
                if (classroom != null) {
                    _createState.value = UiState.Success(classroom)
                    getAllClassrooms()
                } else {
                    _createState.value = UiState.Error("Error creating classroom")
                }
            } catch (e: Exception) {
                _createState.value = UiState.Error(e.message ?: "Error creating classroom")
            }
        }
    }

    fun updateClassroom(id: String, teacherId: String, name: String, description: String) {
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            try {
                val input = UpdateClassroom(
                    teacherId = teacherId,
                    name = name,
                    description = description
                )
                val classroom = repository.updateClassroom(id, input)
                if (classroom != null) {
                    _updateState.value = UiState.Success(classroom)
                    getAllClassrooms()
                } else {
                    _updateState.value = UiState.Error("Error updating classroom")
                }
            } catch (e: Exception) {
                _updateState.value = UiState.Error(e.message ?: "Error updating classroom")
            }
        }
    }

    fun deleteClassroom(id: String) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            try {
                val success = repository.deleteClassroom(id)
                if (success) {
                    _deleteState.value = UiState.Success(true)
                    getAllClassrooms()
                } else {
                    _deleteState.value = UiState.Error("Error deleting classroom")
                }
            } catch (e: Exception) {
                _deleteState.value = UiState.Error(e.message ?: "Error deleting classroom")
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

    fun getAllTeachers() {
        viewModelScope.launch {
            _teachersState.value = UiState.Loading
            try {
                val teachers = teachersRepository.getAllTeachers()
                _teachersState.value = UiState.Success(teachers)
            } catch (e: Exception) {
                _teachersState.value = UiState.Error(e.message ?: "Error getting teachers")
            }
        }
    }
}
