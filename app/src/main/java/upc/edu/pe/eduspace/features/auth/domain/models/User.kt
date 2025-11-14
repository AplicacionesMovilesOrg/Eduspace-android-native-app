package upc.edu.pe.eduspace.features.auth.domain.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val token: String,
    val role: String,
    val username: String
)
