package upc.edu.pe.eduspace.features.classrooms.data.remote.models

data class CreateClassroomRequestDto(
    val teacherId: String,
    val name: String,
    val description: String
)
