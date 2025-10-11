package upc.edu.pe.eduspace.features.teachers.domain.repositories

import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

interface TeachersRepository {
    suspend fun getAllTeachers(): List<Teacher>

    suspend fun createTeacher(input: CreateTeacher): Teacher?
}

data class CreateTeacher(
    val firstName: String, val lastName: String, val email: String, val dni: String,
    val address: String, val phone: String, val username: String, val password: String
)