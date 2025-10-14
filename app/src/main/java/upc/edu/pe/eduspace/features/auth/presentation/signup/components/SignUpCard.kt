package upc.edu.pe.eduspace.features.auth.presentation.signup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import upc.edu.pe.eduspace.core.utils.UiState

@Composable
fun SignUpCard(
    firstName: String,
    lastName: String,
    email: String,
    dni: String,
    address: String,
    phone: String,
    username: String,
    password: String,
    firstNameError: String?,
    lastNameError: String?,
    emailError: String?,
    dniError: String?,
    phoneError: String?,
    usernameError: String?,
    passwordError: String?,
    isPasswordVisible: MutableState<Boolean>,
    signUpState: UiState<*>,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onDniChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "User Registration",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            Spacer(modifier = Modifier.height(24.dp))

            SignUpFormFields(
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
                onFirstNameChange = onFirstNameChange,
                onLastNameChange = onLastNameChange,
                onEmailChange = onEmailChange,
                onDniChange = onDniChange,
                onAddressChange = onAddressChange,
                onPhoneChange = onPhoneChange,
                onUsernameChange = onUsernameChange,
                onPasswordChange = onPasswordChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            SignUpButton(
                onClick = onSignUpClick,
                isLoading = signUpState is UiState.Loading
            )

            if (signUpState is UiState.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = signUpState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LoginLink(onClick = onNavigateToLogin)
        }
    }
}

@Composable
private fun SignUpButton(
    onClick: () -> Unit,
    isLoading: Boolean
) {
    if (isLoading) {
        CircularProgressIndicator(color = Color(0xFF1976D2))
    } else {
        androidx.compose.material3.Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            )
        ) {
            Text(
                text = "Sign Up",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LoginLink(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = "Already have an account?  ",
            color = Color(0xFF424242),
            fontSize = 14.sp
        )
        Text(
            text = "Log In",
            color = Color(0xFF1976D2),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
