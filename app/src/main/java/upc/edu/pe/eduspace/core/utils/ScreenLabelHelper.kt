package upc.edu.pe.eduspace.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import upc.edu.pe.eduspace.R
import upc.edu.pe.eduspace.features.menu.domain.model.Screen

@Composable
fun Screen.getLocalizedLabel(): String {
    return when (this) {
        Screen.HOME -> stringResource(R.string.nav_home)
        Screen.CLASSROOMS -> stringResource(R.string.nav_classrooms)
        Screen.SHARED_SPACES -> stringResource(R.string.nav_shared_spaces)
        Screen.MEETINGS -> stringResource(R.string.nav_meetings)
        Screen.TEACHERS -> stringResource(R.string.nav_teachers)
        Screen.LOGOUT -> stringResource(R.string.logout)
    }
}
