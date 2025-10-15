package upc.edu.pe.eduspace.core.navigation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.ui.components.DrawerMenu
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.ClassroomsRoute
import upc.edu.pe.eduspace.features.home.presentation.home.HomeScreen
import upc.edu.pe.eduspace.features.menu.data.MenuRepositoryImpl
import upc.edu.pe.eduspace.features.menu.domain.GetMenuUseCase
import upc.edu.pe.eduspace.features.menu.domain.model.Screen
import upc.edu.pe.eduspace.features.menu.presentation.SimpleScreen
import upc.edu.pe.eduspace.features.teachers.presentation.teachers.TeachersRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EduSpaceNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val getMenu = remember { GetMenuUseCase(MenuRepositoryImpl()) }
    val menuItems = remember { getMenu() }

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val currentScreen = Screen.entries.find { it.route == currentRoute }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFFFFFF))) {
                DrawerMenu(
                    items = menuItems,
                    current = currentScreen
                ) { item ->
                    scope.launch {
                        if (item.screen == Screen.LOGOUT) {
                            onLogout()
                        } else {
                            navController.navigate(item.screen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(Screen.HOME.route) { saveState = true }
                            }
                        }
                        drawerState.close()
                    }
                }
            }
        }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentScreen?.label ?: "EduSpace") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() }
                        }) { Icon(Icons.Default.Menu, contentDescription = "Menu") }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.HOME.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.HOME.route) { HomeScreen() }
                composable(Screen.CLASSROOMS.route) { ClassroomsRoute() }
                composable(Screen.SHARED_SPACES.route) { SimpleScreen("Shared Spaces") }
                composable(Screen.MEETINGS.route) { SimpleScreen("Meetings") }
                composable(Screen.TEACHERS.route) { TeachersRoute() }
            }
        }
//...
    }
}