package upc.edu.pe.eduspace.features.teachers.data.remote.models

data class TeacherDto(
    val id: Int?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val dni: String?,
    val address: String?,
    val phone: String?,
    val username: String?,
    val administratorId: Int?
)
