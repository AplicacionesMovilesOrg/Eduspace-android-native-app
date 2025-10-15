package upc.edu.pe.eduspace.features.classrooms.data.remote.models

data class UpdateClassroomRequestDto(
    val id: Int,
    val teacherId: Int,
    val name: String,
    val description: String
)
