package upc.edu.pe.eduspace.features.classrooms.domain.models

data class Classroom(
    val id: Int,
    val name: String,
    val description: String,
    val teacherId: Int
)
