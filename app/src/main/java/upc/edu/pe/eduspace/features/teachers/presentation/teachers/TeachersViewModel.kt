package upc.edu.pe.eduspace.features.teachers.presentation.teachers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
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

    init {
        // igual que en HomeViewModel: cargar al iniciar
        getAllTeachers()
    }
}