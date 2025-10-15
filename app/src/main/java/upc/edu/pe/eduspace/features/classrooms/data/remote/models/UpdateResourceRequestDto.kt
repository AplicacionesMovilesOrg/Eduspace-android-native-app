package upc.edu.pe.eduspace.features.classrooms.data.remote.models

data class UpdateResourceRequestDto(
    val id: Int,
    val name: String,
    val kindOfResource: String,
    val classroomId: Int
)
