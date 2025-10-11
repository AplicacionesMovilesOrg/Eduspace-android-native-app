package upc.edu.pe.eduspace.core.navigation


sealed class Route(val route: String) {
    object Teachers : Route("teachers")

}
