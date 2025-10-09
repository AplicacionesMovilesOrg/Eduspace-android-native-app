package upc.edu.pe.eduspace.core.navigation

sealed class Route (val route: String){
    object Login : Route("login")
    object SignUp : Route("signup")
    object Main : Route("main")
}