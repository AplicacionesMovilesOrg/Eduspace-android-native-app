package upc.edu.pe.eduspace.features.classrooms.domain.repositories

import upc.edu.pe.eduspace.features.classrooms.domain.models.Resource

interface ResourcesRepository {
    suspend fun getResourcesByClassroomId(classroomId: Int): List<Resource>

    suspend fun getResourceById(classroomId: Int, resourceId: Int): Resource?

    suspend fun createResource(classroomId: Int, input: CreateResource): Resource?

    suspend fun updateResource(classroomId: Int, resourceId: Int, input: UpdateResource): Resource?

    suspend fun deleteResource(classroomId: Int, resourceId: Int): Boolean
}

data class CreateResource(
    val name: String,
    val kindOfResource: String
)

data class UpdateResource(
    val id: Int,
    val name: String,
    val kindOfResource: String,
    val classroomId: Int
)
