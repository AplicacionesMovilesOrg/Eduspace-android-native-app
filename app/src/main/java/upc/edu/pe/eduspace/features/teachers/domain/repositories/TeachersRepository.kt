package upc.edu.pe.eduspace.features.teachers.domain.repositories

import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

data class UpdateTeacher(
    val firstName: String,
    val lastName: String,
    val email: String,
    val dni: String,
    val address: String,
    val phone: String
)

interface TeachersRepository {
    suspend fun getAllTeachers(): List<Teacher>

    suspend fun getTeacherById(id: String): Teacher?

    suspend fun createTeacher(input: CreateTeacher): Teacher?
    suspend fun updateTeacher(id: String, input: UpdateTeacher): Teacher?
    suspend fun deleteTeacher(id: String): Unit
}

data class CreateTeacher(
    val firstName: String, val lastName: String, val email: String, val dni: String,
    val address: String, val phone: String, val username: String, val password: String
)