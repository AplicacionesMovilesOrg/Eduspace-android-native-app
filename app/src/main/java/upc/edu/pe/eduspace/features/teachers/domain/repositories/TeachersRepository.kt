package upc.edu.pe.eduspace.features.teachers.domain.repositories

import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

interface TeachersRepository {
    suspend fun getAllTeachers(): List<Teacher>
}