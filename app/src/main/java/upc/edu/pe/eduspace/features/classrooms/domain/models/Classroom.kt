package upc.edu.pe.eduspace.features.classrooms.domain.models

data class Classroom(
    val id: String,
    val name: String,
    val description: String,
    val teacherId: String
)
