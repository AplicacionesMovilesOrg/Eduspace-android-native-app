package upc.edu.pe.eduspace.features.menu.domain.model

enum class Screen(val route: String, val label: String) {
    HOME("home", "Home"),
    CLASSROOMS("classrooms", "Classrooms"),
    SHARED_SPACES("shared_spaces", "Shared Spaces"),
    MEETINGS("meetings", "Meetings"),
    TEACHERS("teachers", "Teachers"),
    LOGOUT("logout", "Cerrar Sesión")
}