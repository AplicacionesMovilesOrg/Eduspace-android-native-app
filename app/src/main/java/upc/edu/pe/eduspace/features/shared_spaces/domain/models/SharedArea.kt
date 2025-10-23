package upc.edu.pe.eduspace.features.shared_spaces.domain.models

data class SharedArea(
    val id: String,
    val type: SharedSpaceType,
    val capacity: Int,
    val description: String
)

data class CreateSharedArea(
    val type: SharedSpaceType,
    val capacity: Int,
    val description: String
)

data class UpdateSharedArea(
    val id: String,
    val type: SharedSpaceType,
    val capacity: Int,
    val description: String
)
