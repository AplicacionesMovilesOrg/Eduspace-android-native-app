package upc.edu.pe.eduspace.features.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.home.domain.models.UserHome
import upc.edu.pe.eduspace.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow<UiState<UserHome>>(UiState.Loading)
    val homeState: StateFlow<UiState<UserHome>> = _homeState

    fun loadUserHome() {
        viewModelScope.launch {
            _homeState.value = UiState.Loading
            try {
                val data = repository.getUserHome()
                _homeState.value = UiState.Success(data)
            } catch (e: Exception) {
                _homeState.value = UiState.Error(e.message ?: "Error al cargar los reportes")
            }
        }
    }
}
