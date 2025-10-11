package upc.edu.pe.eduspace.features.auth.data.remote.models

data class SignInRequestDto(
    val username: String,
    val password: String
)

data class AuthenticatedResponseDto(
    val id: Int,
    val username: String,
    val role: String,
    val token: String
)

data class SignUpRequestDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val dni: String,
    val address: String,
    val phone: String,
    val username: String,
    val password: String
)

data class SignUpResponseDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String
)
