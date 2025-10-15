package upc.edu.pe.eduspace.features.classrooms.domain.repositories

import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom

interface ClassroomsRepository {
    suspend fun getAllClassrooms(): List<Classroom>

    suspend fun getClassroomById(id: Int): Classroom?

    suspend fun getClassroomsByTeacherId(teacherId: Int): List<Classroom>

    suspend fun createClassroom(input: CreateClassroom): Classroom?

    suspend fun updateClassroom(id: Int, input: UpdateClassroom): Classroom?

    suspend fun deleteClassroom(id: Int): Boolean
}

data class CreateClassroom(
    val teacherId: Int,
    val name: String,
    val description: String
)

data class UpdateClassroom(
    val teacherId: Int,
    val name: String,
    val description: String
)
