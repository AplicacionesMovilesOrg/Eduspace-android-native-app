package upc.edu.pe.eduspace.core.navigation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.core.data.LanguagePreferences
import upc.edu.pe.eduspace.core.data.SessionManager
import upc.edu.pe.eduspace.core.ui.components.DrawerMenu
import upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.ClassroomDetailRoute
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.ClassroomsRoute
import upc.edu.pe.eduspace.features.home.presentation.home.HomeScreen
import upc.edu.pe.eduspace.features.meetings.presentation.meeting_detail.MeetingDetailRoute
import upc.edu.pe.eduspace.features.meetings.presentation.meetings.MeetingsRoute
import upc.edu.pe.eduspace.features.menu.data.MenuRepositoryImpl
import upc.edu.pe.eduspace.features.menu.domain.GetMenuUseCase
import upc.edu.pe.eduspace.features.menu.domain.model.Screen
import upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_area_detail.SharedAreaDetailRoute
import upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas.SharedAreasRoute
import upc.edu.pe.eduspace.features.teachers.presentation.teachers.TeachersRoute
import upc.edu.pe.eduspace.features.teachers.presentation.teacher_detail.TeacherDetailRoute
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.UpdateTeacher

@Composable
fun EduSpaceNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Language preferences
    val context = LocalContext.current
    val languagePreferences = remember { LanguagePreferences(context) }
    val currentLanguage by languagePreferences.languageFlow.collectAsState(initial = "en")

    // Session manager for logout
    val sessionManager = remember { SessionManager(context) }

    val getMenu = remember { GetMenuUseCase(MenuRepositoryImpl()) }
    val menuItems = remember { getMenu() }

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val currentScreen = Screen.entries.find { it.route == currentRoute }

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f)
                    .background(Color(0xFFFFFFFF))
            ) {
                DrawerMenu(
                    items = menuItems,
                    current = currentScreen,
                    onClick = { item ->
                        scope.launch {
                            if (item.screen == Screen.LOGOUT) {
                                // Clear session on logout
                                sessionManager.clearSession()
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
                    },
                    languagePreferences = languagePreferences,
                    currentLanguage = currentLanguage
                )
            }
        }) {
        NavHost(
            navController = navController,
            startDestination = Screen.HOME.route,
            modifier = Modifier.fillMaxSize()
        ) {
                composable(Screen.HOME.route) {
                    HomeScreen(
                        drawerState = drawerState,
                        scope = scope
                    )
                }
                composable(Screen.CLASSROOMS.route) {
                    ClassroomsRoute(
                        drawerState = drawerState,
                        scope = scope,
                        onClassroomClick = { classroomId ->
                            navController.navigate("classroom_detail/$classroomId")
                        }
                    )
                }
                composable(
                    route = "classroom_detail/{classroomId}",
                    arguments = listOf(
                        navArgument("classroomId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val classroomId = backStackEntry.arguments?.getString("classroomId") ?: ""
                    ClassroomDetailRoute(
                        classroomId = classroomId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.SHARED_SPACES.route) {
                    SharedAreasRoute(
                        drawerState = drawerState,
                        scope = scope,
                        onNavigateToDetail = { sharedAreaId ->
                            navController.navigate("shared_area_detail/$sharedAreaId")
                        }
                    )
                }
                composable(
                    route = "shared_area_detail/{sharedAreaId}",
                    arguments = listOf(
                        navArgument("sharedAreaId") { type = NavType.StringType }
                    )
                ) {
                    SharedAreaDetailRoute(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.MEETINGS.route) {
                    MeetingsRoute(
                        drawerState = drawerState,
                        scope = scope,
                        onNavigateToDetail = { meetingId ->
                            navController.navigate("meeting_detail/$meetingId")
                        }
                    )
                }
                composable(
                    route = "meeting_detail/{meetingId}",
                    arguments = listOf(
                        navArgument("meetingId") { type = NavType.StringType }
                    )
                ) {
                    MeetingDetailRoute(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.TEACHERS.route) {
                    TeachersRoute(
                        drawerState = drawerState,
                        scope = scope,
                        onNavigateToDetail = { teacherId ->
                            navController.navigate("teacher_detail/$teacherId")
                        }
                    )
                }
                composable(
                    route = "teacher_detail/{teacherId}",
                    arguments = listOf(
                        navArgument("teacherId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val teacherId = backStackEntry.arguments?.getString("teacherId") ?: ""
                    // Por ahora usamos un placeholder, en producción deberías obtener el teacher del viewModel
                    val sessionManager = remember { SessionManager(context) }
                    androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel<upc.edu.pe.eduspace.features.teachers.presentation.teachers.TeachersViewModel>().let { viewModel ->
                        val teachersState by viewModel.teachers.collectAsState()
                        LaunchedEffect(Unit) {
                            viewModel.getAllTeachers()
                        }
                        when (val state = teachersState) {
                            is upc.edu.pe.eduspace.core.utils.UiState.Success -> {
                                val teacher = state.data.find { it.id == teacherId }
                                if (teacher != null) {
                                    TeacherDetailRoute(
                                        teacherId = teacherId,
                                        teacher = teacher,
                                        onNavigateBack = { navController.popBackStack() },
                                        onEdit = { id, updateTeacher ->
                                            viewModel.updateTeacher(id, updateTeacher)
                                        },
                                        onDelete = { id ->
                                            viewModel.deleteTeacher(id)
                                            navController.popBackStack()
                                        }
                                    )
                                } else {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                            else -> {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
//...
    }
}