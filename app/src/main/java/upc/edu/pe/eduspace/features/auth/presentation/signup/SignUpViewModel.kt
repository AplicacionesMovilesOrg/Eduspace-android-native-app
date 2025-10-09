package upc.edu.pe.eduspace.features.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.utils.Resource
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignUpRequestDto
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignUpResponseDto
import upc.edu.pe.eduspace.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _dni = MutableStateFlow("")
    val dni: StateFlow<String> = _dni

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _signUpState = MutableStateFlow<UiState<SignUpResponseDto>>(UiState.Initial)
    val signUpState: StateFlow<UiState<SignUpResponseDto>> = _signUpState

    fun updateFirstName(value: String) { _firstName.value = value }
    fun updateLastName(value: String) { _lastName.value = value }
    fun updateEmail(value: String) { _email.value = value }
    fun updateDni(value: String) { _dni.value = value }
    fun updateAddress(value: String) { _address.value = value }
    fun updatePhone(value: String) { _phone.value = value }
    fun updateUsername(value: String) { _username.value = value }
    fun updatePassword(value: String) { _password.value = value }

    fun performSignUp() {
        _signUpState.value = UiState.Loading
        viewModelScope.launch {
            val request = SignUpRequestDto(
                firstName = _firstName.value,
                lastName = _lastName.value,
                email = _email.value,
                dni = _dni.value,
                address = _address.value,
                phone = _phone.value,
                username = _username.value,
                password = _password.value
            )

            when (val result = repository.signUp(request)) {
                is Resource.Success -> _signUpState.value = UiState.Success(result.data!!)
                is Resource.Error -> _signUpState.value = UiState.Error(result.message ?: "Unknown error")
                else -> { }
            }
        }
    }
}