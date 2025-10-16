package upc.edu.pe.eduspace.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.data.SessionManager
import upc.edu.pe.eduspace.core.utils.Resource
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.auth.domain.models.User
import upc.edu.pe.eduspace.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _user = MutableStateFlow<UiState<User>>(UiState.Initial)
    val user: StateFlow<UiState<User>> = _user

    fun updateUsername(username: String) {
        _username.value = username
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun login() {
        _user.value = UiState.Loading
        viewModelScope.launch {
            val resource = repository.signIn(
                username.value,
                password.value
            )

            when (resource) {
                is Resource.Success -> {
                    val user = resource.data as User
                    // Save complete session
                    sessionManager.saveSession(user.id, username.value)
                    _user.value = UiState.Success(user)
                }
                is Resource.Error -> _user.value = UiState.Error(resource.message as String)
                is Resource.Loading -> _user.value = UiState.Loading
            }
        }
    }
}