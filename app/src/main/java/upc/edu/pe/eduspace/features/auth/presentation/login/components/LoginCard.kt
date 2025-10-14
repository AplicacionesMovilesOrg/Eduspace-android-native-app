package upc.edu.pe.eduspace.features.auth.presentation.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import upc.edu.pe.eduspace.core.utils.UiState

@Composable
fun LoginCard(
    username: String,
    password: String,
    isPasswordVisible: MutableState<Boolean>,
    loginState: UiState<*>,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToSignUp: () -> Unit
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoginFormFields(
                username = username,
                password = password,
                isPasswordVisible = isPasswordVisible,
                onUsernameChange = onUsernameChange,
                onPasswordChange = onPasswordChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            LoginButton(
                onClick = onLoginClick,
                enabled = loginState !is UiState.Loading
            )

            LoginStateIndicators(loginState)

            Spacer(modifier = Modifier.height(16.dp))

            SignUpLink(onClick = onNavigateToSignUp)
        }
    }
}

@Composable
private fun LoginFormFields(
    username: String,
    password: String,
    isPasswordVisible: MutableState<Boolean>,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Email or Username") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1976D2),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color(0xFFFAFAFA)
        ),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Password") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                Icon(
                    imageVector = if (isPasswordVisible.value)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color(0xFF757575)
                )
            }
        },
        visualTransformation = if (isPasswordVisible.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1976D2),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color(0xFFFAFAFA)
        ),
        singleLine = true
    )
}

@Composable
private fun LoginButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1976D2)
        ),
        enabled = enabled
    ) {
        Text(
            text = "Sign In",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun LoginStateIndicators(loginState: UiState<*>) {
    if (loginState is UiState.Loading) {
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF1976D2)
        )
    }

    if (loginState is UiState.Error) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = loginState.message,
            color = MaterialTheme.colorScheme.error,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SignUpLink(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = "New here?  ",
            color = Color(0xFF424242),
            fontSize = 14.sp
        )
        Text(
            text = "Sign Up",
            color = Color(0xFF1976D2),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}