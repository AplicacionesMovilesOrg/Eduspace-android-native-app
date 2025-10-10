package upc.edu.pe.eduspace.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import upc.edu.pe.eduspace.features.auth.presentation.login.Login
import upc.edu.pe.eduspace.features.auth.presentation.signup.SignUp
import upc.edu.pe.eduspace.features.home.presentation.home.HomeScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Login.route
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
            println("✅ Navegando a Home desde AppNav")
            HomeScreen(onMenuClick = { /* TODO abrir drawer */ })
        }
    }
}
