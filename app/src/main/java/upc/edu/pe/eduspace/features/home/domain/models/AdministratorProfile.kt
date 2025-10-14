package upc.edu.pe.eduspace.features.home.domain.models

data class AdministratorProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val dni: String,
    val address: String,
    val phone: String
)
