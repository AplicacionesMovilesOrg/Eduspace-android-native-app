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
                Log.e("ClassroomsRepository", "Error getting classrooms: ${response.message()}")
                return@withContext emptyList()
            }

            val list = response.body() ?: emptyList<ClassroomDto>()
            return@withContext list.mapNotNull { dto -> dto.toDomain() }
        } catch (e: Exception) {
            Log.e("ClassroomsRepository", "Exception getting classrooms", e)
            return@withContext emptyList()
        }
    }

    override suspend fun getClassroomById(id: Int): Classroom? = withContext(Dispatchers.IO) {
        try {
            val response = service.getClassroomById(id)
            if (!response.isSuccessful) {
                Log.e("ClassroomsRepository", "Error getting classroom by id: ${response.message()}")
                return@withContext null
            }

            val dto = response.body()
            return@withContext dto?.toDomain()
        } catch (e: Exception) {
            Log.e("ClassroomsRepository", "Exception getting classroom by id", e)
            return@withContext null
        }
    }

    override suspend fun getClassroomsByTeacherId(teacherId: Int): List<Classroom> = withContext(Dispatchers.IO) {
        try {
            val response = service.getClassroomsByTeacherId(teacherId)
            if (!response.isSuccessful) {
                Log.e("ClassroomsRepository", "Error getting classrooms by teacher: ${response.message()}")
                return@withContext emptyList()
            }

            val list = response.body() ?: emptyList<ClassroomDto>()
            return@withContext list.mapNotNull { dto -> dto.toDomain() }
        } catch (e: Exception) {
            Log.e("ClassroomsRepository", "Exception getting classrooms by teacher", e)
            return@withContext emptyList()
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

    override suspend fun updateClassroom(id: Int, input: UpdateClassroom): Classroom? = withContext(Dispatchers.IO) {
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

    override suspend fun deleteClassroom(id: Int): Boolean = withContext(Dispatchers.IO) {
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
