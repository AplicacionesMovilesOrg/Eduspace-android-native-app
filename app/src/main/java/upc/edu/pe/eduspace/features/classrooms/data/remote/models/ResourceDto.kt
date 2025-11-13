package upc.edu.pe.eduspace.features.classrooms.data.remote.models

import com.google.gson.annotations.SerializedName

data class ResourceDto(
    val id: String?,
    val name: String?,
    val kindOfResource: String?,
    @SerializedName("classroomId")
    val classroomId: String?,
    val classroom: ClassroomNestedDto?
)

data class ClassroomNestedDto(
    val id: String?,
    val name: String?,
    val description: String?,
    val teacherId: String?
)
