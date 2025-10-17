package upc.edu.pe.eduspace.features.menu.domain.model

import androidx.annotation.StringRes
import upc.edu.pe.eduspace.R

enum class Screen(val route: String, @StringRes val label: Int) {
    HOME("home", R.string.screen_home),
    CLASSROOMS("classrooms", R.string.screen_classrooms),
    SHARED_SPACES("shared_spaces", R.string.screen_shared_spaces),
    MEETINGS("meetings", R.string.screen_meetings),
    TEACHERS("teachers", R.string.screen_teachers),
    LOGOUT("logout", R.string.screen_logout)
}