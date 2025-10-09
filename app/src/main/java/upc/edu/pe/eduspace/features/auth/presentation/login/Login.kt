package upc.edu.pe.eduspace.features.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import upc.edu.pe.eduspace.core.ui.theme.AppTheme
import upc.edu.pe.eduspace.core.utils.UiState

@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val userState = viewModel.user.collectAsState()
    val username = viewModel.username.collectAsState()
    val password = viewModel.password.collectAsState()

    LaunchedEffect(key1 = userState.value) {
        if (userState.value is UiState.Success) {
            onLogin()
        }
    }

    val isVisible = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            username.value,
            onValueChange = {
                viewModel.updateUsername(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null
                )
            },
            placeholder = {
                Text(text = "Email")
            }
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = {
                viewModel.updatePassword(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null
                )
            },
            placeholder = {
                Text(text = "Password")
            },
            visualTransformation =
                if (isVisible.value) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

            trailingIcon = {
                IconButton(
                    onClick = {
                        isVisible.value = !isVisible.value
                    }
                ) {
                    Icon(
                        if (isVisible.value) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = null
                    )
                }
            }
        )

        Button(
            onClick = {
                viewModel.login()
                      },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Sign in")
        }

        when (val state = userState.value) {
            is UiState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp))
            }
            is UiState.Success<*> -> {
            }
            is UiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            is UiState.Initial -> {}
        }

        TextButton(
            onClick = onNavigateToSignUp,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Are you a new user? Sign up")
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    AppTheme(dynamicColor = false) {
        Login (
            onLogin = {},
            onNavigateToSignUp = {}
        )
    }
}