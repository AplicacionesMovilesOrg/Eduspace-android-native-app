package upc.edu.pe.eduspace.features.classrooms.data.remote.models

data class UpdateClassroomRequestDto(
    val id: String,
    val teacherId: String,
    val name: String,
    val description: String
)
