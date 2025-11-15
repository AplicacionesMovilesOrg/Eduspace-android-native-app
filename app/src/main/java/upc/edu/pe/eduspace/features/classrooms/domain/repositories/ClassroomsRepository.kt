package upc.edu.pe.eduspace.features.classrooms.domain.repositories

import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom

interface ClassroomsRepository {
    suspend fun getAllClassrooms(): List<Classroom>

    suspend fun getClassroomById(id: String): Classroom?

    suspend fun getClassroomsByTeacherId(teacherId: String): List<Classroom>

    suspend fun createClassroom(input: CreateClassroom): Classroom?

    suspend fun updateClassroom(id: String, input: UpdateClassroom): Classroom?

    suspend fun deleteClassroom(id: String): Boolean
}

data class CreateClassroom(
    val teacherId: String,
    val name: String,
    val description: String
)

data class UpdateClassroom(
    val teacherId: String,
    val name: String,
    val description: String
)
