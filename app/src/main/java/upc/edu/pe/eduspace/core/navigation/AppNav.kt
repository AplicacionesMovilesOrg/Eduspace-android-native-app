package upc.edu.pe.eduspace.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import upc.edu.pe.eduspace.core.data.SessionManager
import upc.edu.pe.eduspace.features.auth.presentation.login.Login
import upc.edu.pe.eduspace.features.auth.presentation.signup.SignUp
import javax.inject.Inject

@HiltViewModel
class AppNavViewModel @Inject constructor(
    sessionManager: SessionManager
) : ViewModel() {
    val isLoggedIn: Flow<Boolean> = sessionManager.isLoggedInFlow
}

@Composable
fun AppNav(viewModel: AppNavViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)

    // Set initial destination based on login state
    val startDestination = if (isLoggedIn) Route.Main.route else Route.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.Login.route) {
            Login(
                onLogin = {
                    navController.navigate(Route.Main.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Route.SignUp.route)
                }
            )
        }

        composable(Route.SignUp.route) {
            SignUp(
                onSignUpSuccess = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Route.Main.route) {
            EduSpaceNavigation(
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}