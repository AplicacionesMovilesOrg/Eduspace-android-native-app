package upc.edu.pe.eduspace.features.teachers.presentation.teachers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.CreateTeacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.TeachersRepository
import javax.inject.Inject

@HiltViewModel
class TeachersViewModel @Inject constructor(
    private val repository: TeachersRepository
) : ViewModel() {

    private val _teachers = MutableStateFlow(emptyList<Teacher>())
    val teachers: StateFlow<List<Teacher>> = _teachers

    fun getAllTeachers() {
        viewModelScope.launch {
            _teachers.value = repository.getAllTeachers()
        }
    }

    fun createTeacher(input: CreateTeacher, onDone: () -> Unit, onError: (String)->Unit) {
        viewModelScope.launch {
            runCatching { repository.createTeacher(input) }
                .onSuccess { created ->
                    if (created != null) {
                        _teachers.update {
                            it + created
                        }
                        onDone()
                    } else onError("Could not create teacher")
                }
                .onFailure { e -> onError(e.message ?: "Error creating teacher") }
        }
    }

    init {
        getAllTeachers()
    }
}