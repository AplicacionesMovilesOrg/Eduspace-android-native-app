package upc.edu.pe.eduspace.features.teachers.data.remote.models

import com.google.gson.annotations.SerializedName

data class UpdateTeacherRequestDto(
    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("dni")
    val dni: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("phone")
    val phone: String
)