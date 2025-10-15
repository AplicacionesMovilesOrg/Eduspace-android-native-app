package upc.edu.pe.eduspace.features.teachers.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import upc.edu.pe.eduspace.features.teachers.data.remote.models.CreateTeacherRequestDto
import upc.edu.pe.eduspace.features.teachers.data.remote.models.TeacherDto
import upc.edu.pe.eduspace.features.teachers.data.remote.services.TeachersService
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.CreateTeacher
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

        return@withContext list.mapNotNull { dto ->
            val id = dto.id ?: return@mapNotNull null
            Teacher(
                id = id,
                firstName = dto.firstName.orEmpty(),
                lastName  = dto.lastName.orEmpty(),
                email     = dto.email.orEmpty(),
                dni       = dto.dni.orEmpty(),
                address   = dto.address.orEmpty(),
                phone     = dto.phone.orEmpty()
            )
        }

    }

    override suspend fun getTeacherById(id: Int): Teacher? = withContext(Dispatchers.IO) {
        try {
            val response = service.getTeacherById(id)
            if (!response.isSuccessful) {
                Log.e("TeachersRepository", "Error getting teacher by id: ${response.message()}")
                return@withContext null
            }

            val dto = response.body() ?: return@withContext null
            val teacherId = dto.id ?: return@withContext null

            return@withContext Teacher(
                id = teacherId,
                firstName = dto.firstName.orEmpty(),
                lastName = dto.lastName.orEmpty(),
                email = dto.email.orEmpty(),
                dni = dto.dni.orEmpty(),
                address = dto.address.orEmpty(),
                phone = dto.phone.orEmpty()
            )
        } catch (e: Exception) {
            Log.e("TeachersRepository", "Exception getting teacher by id: ${e.message}", e)
            return@withContext null
        }
    }

    override suspend fun createTeacher(input: CreateTeacher): Teacher? {
        val req = CreateTeacherRequestDto(
            firstName = input.firstName, lastName = input.lastName,
            email = input.email, dni = input.dni, address = input.address,
            phone = input.phone, username = input.username, password = input.password
        )
        val r = service.createTeacher(req)
        if (!r.isSuccessful) return null
        val dto = r.body() ?: return null
        val id = dto.id ?: return null
        return Teacher(
            id = id,
            firstName = dto.firstName.orEmpty(),
            lastName  = dto.lastName.orEmpty(),
            email     = dto.email.orEmpty(),
            dni       = dto.dni.orEmpty(),
            address   = dto.address.orEmpty(),
            phone     = dto.phone.orEmpty()
        )
    }
}