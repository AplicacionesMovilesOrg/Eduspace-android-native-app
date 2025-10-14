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

    // Estados de error para cada campo
    private val _firstNameError = MutableStateFlow<String?>(null)
    val firstNameError: StateFlow<String?> = _firstNameError

    private val _lastNameError = MutableStateFlow<String?>(null)
    val lastNameError: StateFlow<String?> = _lastNameError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _dniError = MutableStateFlow<String?>(null)
    val dniError: StateFlow<String?> = _dniError

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError

    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError: StateFlow<String?> = _usernameError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _signUpState = MutableStateFlow<UiState<SignUpResponseDto>>(UiState.Initial)
    val signUpState: StateFlow<UiState<SignUpResponseDto>> = _signUpState

    fun updateFirstName(value: String) {
        _firstName.value = value
        _firstNameError.value = null
    }

    fun updateLastName(value: String) {
        _lastName.value = value
        _lastNameError.value = null
    }

    fun updateEmail(value: String) {
        _email.value = value
        _emailError.value = null
    }

    fun updateDni(value: String) {
        // Solo permitir números y máximo 8 dígitos
        if (value.isEmpty() || (value.all { it.isDigit() } && value.length <= 8)) {
            _dni.value = value
            _dniError.value = null
        }
    }

    fun updateAddress(value: String) {
        _address.value = value
    }

    fun updatePhone(value: String) {
        // Solo permitir números y máximo 9 dígitos
        if (value.isEmpty() || (value.all { it.isDigit() } && value.length <= 9)) {
            _phone.value = value
            _phoneError.value = null
        }
    }

    fun updateUsername(value: String) {
        _username.value = value
        _usernameError.value = null
    }

    fun updatePassword(value: String) {
        _password.value = value
        _passwordError.value = null
    }

    private fun validateFirstName(): Boolean {
        return when {
            _firstName.value.isBlank() -> {
                _firstNameError.value = "El nombre es obligatorio"
                false
            }
            else -> {
                _firstNameError.value = null
                true
            }
        }
    }

    private fun validateLastName(): Boolean {
        return when {
            _lastName.value.isBlank() -> {
                _lastNameError.value = "El apellido es obligatorio"
                false
            }
            else -> {
                _lastNameError.value = null
                true
            }
        }
    }

    private fun validateEmail(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return when {
            _email.value.isBlank() -> {
                _emailError.value = "El correo es obligatorio"
                false
            }
            !_email.value.matches(emailPattern.toRegex()) -> {
                _emailError.value = "Formato de correo inválido"
                false
            }
            else -> {
                _emailError.value = null
                true
            }
        }
    }

    private fun validateDni(): Boolean {
        return when {
            _dni.value.isBlank() -> {
                _dniError.value = "El DNI es obligatorio"
                false
            }
            _dni.value.length != 8 -> {
                _dniError.value = "El DNI debe tener 8 dígitos"
                false
            }
            !_dni.value.all { it.isDigit() } -> {
                _dniError.value = "El DNI solo debe contener números"
                false
            }
            else -> {
                _dniError.value = null
                true
            }
        }
    }

    private fun validatePhone(): Boolean {
        return when {
            _phone.value.isBlank() -> {
                _phoneError.value = "El teléfono es obligatorio"
                false
            }
            _phone.value.length != 9 -> {
                _phoneError.value = "El teléfono debe tener 9 dígitos"
                false
            }
            !_phone.value.startsWith("9") -> {
                _phoneError.value = "El teléfono debe comenzar con 9"
                false
            }
            !_phone.value.all { it.isDigit() } -> {
                _phoneError.value = "El teléfono solo debe contener números"
                false
            }
            else -> {
                _phoneError.value = null
                true
            }
        }
    }

    private fun validateUsername(): Boolean {
        return when {
            _username.value.isBlank() -> {
                _usernameError.value = "El nombre de usuario es obligatorio"
                false
            }
            _username.value.length < 4 -> {
                _usernameError.value = "El usuario debe tener al menos 4 caracteres"
                false
            }
            else -> {
                _usernameError.value = null
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val password = _password.value
        return when {
            password.isBlank() -> {
                _passwordError.value = "La contraseña es obligatoria"
                false
            }
            password.length < 8 -> {
                _passwordError.value = "La contraseña debe tener al menos 8 caracteres"
                false
            }
            !password.any { it.isUpperCase() } -> {
                _passwordError.value = "Debe contener al menos una mayúscula"
                false
            }
            !password.any { it.isDigit() } -> {
                _passwordError.value = "Debe contener al menos un número"
                false
            }
            !password.any { !it.isLetterOrDigit() } -> {
                _passwordError.value = "Debe contener al menos un carácter especial"
                false
            }
            else -> {
                _passwordError.value = null
                true
            }
        }
    }

    fun performSignUp() {
        // Validar todos los campos
        val isFirstNameValid = validateFirstName()
        val isLastNameValid = validateLastName()
        val isEmailValid = validateEmail()
        val isDniValid = validateDni()
        val isPhoneValid = validatePhone()
        val isUsernameValid = validateUsername()
        val isPasswordValid = validatePassword()

        // Si algún campo no es válido, no continuar
        if (!isFirstNameValid || !isLastNameValid || !isEmailValid ||
            !isDniValid || !isPhoneValid || !isUsernameValid || !isPasswordValid) {
            return
        }

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
                is Resource.Error -> _signUpState.value = UiState.Error(result.message ?: "Error desconocido")
                else -> { }
            }
        }
    }
}