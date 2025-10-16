package upc.edu.pe.eduspace.features.teachers.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import upc.edu.pe.eduspace.features.teachers.data.remote.models.CreateTeacherRequestDto
import upc.edu.pe.eduspace.features.teachers.data.remote.services.TeachersService
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.CreateTeacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.TeachersRepository
import javax.inject.Inject

class TeachersRepositoryImpl @Inject constructor(
    private val service: TeachersService
) : TeachersRepository {
    override suspend fun getAllTeachers(): List<Teacher> = withContext(Dispatchers.IO) {
        try {
            val response = service.getAllTeachers()

            if (!response.isSuccessful) {
                val errorMsg = "Failed to load teachers: ${response.message()}"
                Log.e("TeachersRepository", errorMsg)
                throw Exception(errorMsg)
            }

            val list = response.body() ?: throw Exception("Empty response body")
            Log.d("TeachersRepository", "Successfully loaded ${list.size} teachers")

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
        } catch (e: Exception) {
            Log.e("TeachersRepository", "Error loading teachers", e)
            throw e
        }
    }

    override suspend fun getTeacherById(id: Int): Teacher? = withContext(Dispatchers.IO) {
        try {
            val response = service.getTeacherById(id)

            if (!response.isSuccessful) {
                val errorMsg = "Failed to load teacher: ${response.message()}"
                Log.e("TeachersRepository", errorMsg)
                throw Exception(errorMsg)
            }

            val dto = response.body() ?: throw Exception("Empty response body")
            val teacherId = dto.id ?: throw Exception("Teacher ID is null")

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
            Log.e("TeachersRepository", "Error loading teacher by id", e)
            throw e
        }
    }

    override suspend fun createTeacher(input: CreateTeacher): Teacher? = withContext(Dispatchers.IO) {
        try {
            val req = CreateTeacherRequestDto(
                firstName = input.firstName, lastName = input.lastName,
                email = input.email, dni = input.dni, address = input.address,
                phone = input.phone, username = input.username, password = input.password
            )

            val response = service.createTeacher(req)

            if (!response.isSuccessful) {
                val errorMsg = "Failed to create teacher: ${response.message()}"
                Log.e("TeachersRepository", errorMsg)
                throw Exception(errorMsg)
            }

            val dto = response.body() ?: throw Exception("Empty response body")
            val id = dto.id ?: throw Exception("Teacher ID is null")

            Log.d("TeachersRepository", "Teacher created successfully with id: $id")

            return@withContext Teacher(
                id = id,
                firstName = dto.firstName.orEmpty(),
                lastName  = dto.lastName.orEmpty(),
                email     = dto.email.orEmpty(),
                dni       = dto.dni.orEmpty(),
                address   = dto.address.orEmpty(),
                phone     = dto.phone.orEmpty()
            )
        } catch (e: Exception) {
            Log.e("TeachersRepository", "Error creating teacher", e)
            throw e
        }
    }
}