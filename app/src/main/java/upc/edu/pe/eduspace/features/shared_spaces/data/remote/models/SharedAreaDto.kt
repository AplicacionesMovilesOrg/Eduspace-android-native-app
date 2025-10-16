package upc.edu.pe.eduspace.features.shared_spaces.data.remote.models

import com.google.gson.annotations.SerializedName
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedSpaceType

data class SharedAreaDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("capacity")
    val capacity: Int?,
    @SerializedName("description")
    val description: String?
) {
    fun toDomain(): SharedArea {
        return SharedArea(
            id = id ?: 0,
            type = SharedSpaceType.fromString(name ?: ""),
            capacity = capacity ?: 0,
            description = description ?: ""
        )
    }
}

data class CreateSharedAreaRequestDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("capacity")
    val capacity: Int,
    @SerializedName("description")
    val description: String
)

data class UpdateSharedAreaRequestDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("capacity")
    val capacity: Int,
    @SerializedName("description")
    val description: String
)
