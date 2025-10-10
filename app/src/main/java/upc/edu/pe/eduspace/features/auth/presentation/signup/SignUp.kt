package upc.edu.pe.eduspace.features.auth.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.auth.presentation.login.components.GradientBackground
import upc.edu.pe.eduspace.features.auth.presentation.signup.components.SignUpCard

@Composable
fun SignUp(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email by viewModel.email.collectAsState()
    val dni by viewModel.dni.collectAsState()
    val address by viewModel.address.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()

    val firstNameError by viewModel.firstNameError.collectAsState()
    val lastNameError by viewModel.lastNameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val dniError by viewModel.dniError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val usernameError by viewModel.usernameError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    val signUpState by viewModel.signUpState.collectAsState()

    val isPasswordVisible = remember { mutableStateOf(false) }

    LaunchedEffect(signUpState) {
        if (signUpState is UiState.Success) {
            onSignUpSuccess()
        }
    }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            SignUpCard(
                firstName = firstName,
                lastName = lastName,
                email = email,
                dni = dni,
                address = address,
                phone = phone,
                username = username,
                password = password,
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                emailError = emailError,
                dniError = dniError,
                phoneError = phoneError,
                usernameError = usernameError,
                passwordError = passwordError,
                isPasswordVisible = isPasswordVisible,
                signUpState = signUpState,
                onFirstNameChange = viewModel::updateFirstName,
                onLastNameChange = viewModel::updateLastName,
                onEmailChange = viewModel::updateEmail,
                onDniChange = viewModel::updateDni,
                onAddressChange = viewModel::updateAddress,
                onPhoneChange = viewModel::updatePhone,
                onUsernameChange = viewModel::updateUsername,
                onPasswordChange = viewModel::updatePassword,
                onSignUpClick = viewModel::performSignUp,
                onNavigateToLogin = onNavigateToLogin
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}