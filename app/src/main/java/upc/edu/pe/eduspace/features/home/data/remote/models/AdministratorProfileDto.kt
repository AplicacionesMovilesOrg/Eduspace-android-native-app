package upc.edu.pe.eduspace.features.home.data.remote.models

import upc.edu.pe.eduspace.features.home.domain.models.AdministratorProfile

data class AdministratorProfileDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val dni: String,
    val address: String,
    val phone: String
)

fun AdministratorProfileDto.toDomain() = AdministratorProfile(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    dni = dni,
    address = address,
    phone = phone
)
