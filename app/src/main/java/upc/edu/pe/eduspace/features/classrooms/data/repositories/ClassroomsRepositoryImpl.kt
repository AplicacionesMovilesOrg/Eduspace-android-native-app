package upc.edu.pe.eduspace.features.classrooms.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.ClassroomDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.CreateClassroomRequestDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.UpdateClassroomRequestDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.services.ClassroomsService
import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.ClassroomsRepository
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.CreateClassroom
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.UpdateClassroom
import javax.inject.Inject

class ClassroomsRepositoryImpl @Inject constructor(
    private val service: ClassroomsService
) : ClassroomsRepository {

    override suspend fun getAllClassrooms(): List<Classroom> = withContext(Dispatchers.IO) {
        try {
            val response = service.getAllClassrooms()

            if (!response.isSuccessful) {
                val errorMsg = "Failed to load classrooms: ${response.message()}"
                Log.e("ClassroomsRepository", errorMsg)
                throw Exception(errorMsg)
            }

            val list = response.body() ?: throw Exception("Empty response body")
            Log.d("ClassroomsRepository", "Successfully loaded ${list.size} classrooms")

            return@withContext list.mapNotNull { dto -> dto.toDomain() }
        } catch (e: Exception) {
            Log.e("ClassroomsRepository", "Error loading classrooms", e)
            throw e
        }
    }

    override suspend fun getClassroomById(id: String): Classroom? = withContext(Dispatchers.IO) {
        try {
            val response = service.getClassroomById(id)

            if (!response.isSuccessful) {
                val errorMsg = "Failed to load classroom: ${response.message()}"
                Log.e("ClassroomsRepository", errorMsg)
                throw Exception(errorMsg)
            }

            val dto = response.body() ?: throw Exception("Empty response body")
            return@withContext dto.toDomain()
        } catch (e: Exception) {
            Log.e("ClassroomsRepository", "Error loading classroom by id", e)
            throw e
        }
    }

    override suspend fun getClassroomsByTeacherId(teacherId: String): List<Classroom> = withContext(Dispatchers.IO) {
        try {
            val response = service.getClassroomsByTeacherId(teacherId)

            if (!response.isSuccessful) {
                val errorMsg = "Failed to load classrooms for teacher: ${response.message()}"
                Log.e("ClassroomsRepository", errorMsg)
                throw Exception(errorMsg)
            }

            val list = response.body() ?: throw Exception("Empty response body")
            Log.d("ClassroomsRepository", "Successfully loaded ${list.size} classrooms for teacher $teacherId")

            return@withContext list.mapNotNull { dto -> dto.toDomain() }
        } catch (e: Exception) {
            Log.e("ClassroomsRepository", "Error loading classrooms for teacher", e)
            throw e
        }
    }

    override suspend fun createClassroom(input: CreateClassroom): Classroom? = withContext(Dispatchers.IO) {
        try {
            val request = CreateClassroomRequestDto(
                teacherId = input.teacherId,
                name = input.name,
                description = input.description
            )

            val response = service.createClassroom(
                teacherId = input.teacherId,
                body = request
            )

            if (!response.isSuccessful) {
                Log.e("ClassroomsRepository", "Error creating classroom: ${response.message()}")
                return@withContext null
            }

            val dto = response.body()
            return@withContext dto?.toDomain()
        } catch (e: Exception) {
            Log.e("ClassroomsRepository", "Exception creating classroom", e)
            return@withContext null
        }
    }

    override suspend fun updateClassroom(id: String, input: UpdateClassroom): Classroom? = withContext(Dispatchers.IO) {
        try {
            val request = UpdateClassroomRequestDto(
                id = id,
                teacherId = input.teacherId,
                name = input.name,
                description = input.description
            )

            Log.d("ClassroomsRepository", "Updating classroom $id with: id=$id, teacherId=${input.teacherId}, name=${input.name}, description=${input.description}")

            val response = service.updateClassroom(id, request)

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e("ClassroomsRepository", "Error updating classroom: code=${response.code()}, message=${response.message()}, error=$errorBody")
                return@withContext null
            }

            val dto = response.body()
            Log.d("ClassroomsRepository", "Classroom updated successfully: $dto")
            return@withContext dto?.toDomain()
        } catch (e: Exception) {
            Log.e("ClassroomsRepository", "Exception updating classroom: ${e.message}", e)
            return@withContext null
        }
    }

    override suspend fun deleteClassroom(id: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = service.deleteClassroom(id)

            if (!response.isSuccessful) {
                Log.e("ClassroomsRepository", "Error deleting classroom: ${response.message()}")
                return@withContext false
            }

            return@withContext true
        } catch (e: Exception) {
            Log.e("ClassroomsRepository", "Exception deleting classroom", e)
            return@withContext false
        }
    }

    private fun ClassroomDto.toDomain(): Classroom? {
        val id = this.id ?: return null
        val name = this.name ?: return null
        val description = this.description ?: return null
        val teacherId = this.teacherId ?: return null

        return Classroom(
            id = id,
            name = name,
            description = description,
            teacherId = teacherId
        )
    }
}
