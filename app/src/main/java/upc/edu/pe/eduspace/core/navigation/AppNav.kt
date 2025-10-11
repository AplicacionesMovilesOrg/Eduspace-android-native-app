package upc.edu.pe.eduspace.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import upc.edu.pe.eduspace.features.teachers.presentation.teachers.TeachersRoute

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Teachers.route
    ) {
        composable(Route.Teachers.route) {
            TeachersRoute()
        }
    }
}