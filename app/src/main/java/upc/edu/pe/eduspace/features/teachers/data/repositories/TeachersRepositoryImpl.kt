package upc.edu.pe.eduspace.features.teachers.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import upc.edu.pe.eduspace.features.teachers.data.remote.models.TeacherDto
import upc.edu.pe.eduspace.features.teachers.data.remote.services.TeachersService
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.TeachersRepository
import javax.inject.Inject

class TeachersRepositoryImpl @Inject constructor(
    private val service: TeachersService
) : TeachersRepository {
    override suspend fun getAllTeachers(): List<Teacher> = withContext(Dispatchers.IO){

        val response = service.getAllTeachers()
        if (!response.isSuccessful) return@withContext emptyList()
        Log.d("TeachersRepository", response.message())

        val list = response.body() ?: emptyList<TeacherDto>()

        return@withContext list.map { dto ->
            Teacher(
                firstName = dto.firstName.orEmpty(),
                lastName  = dto.lastName.orEmpty(),
                email     = dto.email.orEmpty(),
                dni       = dto.dni.orEmpty()
            )
        }

    }
}