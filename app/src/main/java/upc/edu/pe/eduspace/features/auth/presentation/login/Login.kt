package upc.edu.pe.eduspace.features.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.auth.presentation.login.components.GradientBackground
import upc.edu.pe.eduspace.features.auth.presentation.login.components.LoginCard

@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val userState by viewModel.user.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()

    LaunchedEffect(key1 = userState) {
        if (userState is UiState.Success) {
            onLogin()
        }
    }

    val isPasswordVisible = remember { mutableStateOf(false) }

    GradientBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginCard(
                username = username,
                password = password,
                isPasswordVisible = isPasswordVisible,
                loginState = userState,
                onUsernameChange = viewModel::updateUsername,
                onPasswordChange = viewModel::updatePassword,
                onLoginClick = viewModel::login,
                onNavigateToSignUp = onNavigateToSignUp
            )
        }
    }
}