package upc.edu.pe.eduspace.features.teachers.domain.model

data class Teacher(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val dni: String,
    val address: String,
    val phone: String
)

