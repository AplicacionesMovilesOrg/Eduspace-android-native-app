package upc.edu.pe.eduspace.features.classrooms.domain.repositories

import upc.edu.pe.eduspace.features.classrooms.domain.models.Resource

interface ResourcesRepository {
    suspend fun getResourcesByClassroomId(classroomId: String): List<Resource>

    suspend fun getResourceById(classroomId: String, resourceId: String): Resource?

    suspend fun createResource(classroomId: String, input: CreateResource): Resource?

    suspend fun updateResource(classroomId: String, resourceId: String, input: UpdateResource): Resource?

    suspend fun deleteResource(classroomId: String, resourceId: String): Boolean
}

data class CreateResource(
    val name: String,
    val kindOfResource: String
)

data class UpdateResource(
    val id: String,
    val name: String,
    val kindOfResource: String,
    val classroomId: String
)
