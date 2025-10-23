package upc.edu.pe.eduspace.features.classrooms.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.CreateResourceRequestDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.ResourceDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.UpdateResourceRequestDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.services.ResourcesService
import upc.edu.pe.eduspace.features.classrooms.domain.models.Resource
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.CreateResource
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.ResourcesRepository
import upc.edu.pe.eduspace.features.classrooms.domain.repositories.UpdateResource
import javax.inject.Inject

class ResourcesRepositoryImpl @Inject constructor(
    private val service: ResourcesService
) : ResourcesRepository {

    override suspend fun getResourcesByClassroomId(classroomId: String): List<Resource> = withContext(Dispatchers.IO) {
        try {
            Log.d("ResourcesRepository", "Fetching resources for classroom $classroomId")
            val response = service.getResourcesByClassroomId(classroomId)

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e("ResourcesRepository", "Error getting resources: code=${response.code()}, message=${response.message()}, error=$errorBody")
                return@withContext emptyList()
            }

            val list = response.body() ?: emptyList<ResourceDto>()
            Log.d("ResourcesRepository", "Received ${list.size} resources from API: $list")

            val domainList = list.mapNotNull { dto ->
                val domain = dto.toDomain()
                if (domain == null) {
                    Log.w("ResourcesRepository", "Failed to convert DTO to domain: $dto")
                }
                domain
            }

            Log.d("ResourcesRepository", "Successfully converted ${domainList.size} resources")
            return@withContext domainList
        } catch (e: Exception) {
            Log.e("ResourcesRepository", "Exception getting resources: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    override suspend fun getResourceById(classroomId: String, resourceId: String): Resource? = withContext(Dispatchers.IO) {
        try {
            val response = service.getResourceById(classroomId, resourceId)
            if (!response.isSuccessful) {
                Log.e("ResourcesRepository", "Error getting resource by id: ${response.message()}")
                return@withContext null
            }

            val dto = response.body()
            return@withContext dto?.toDomain()
        } catch (e: Exception) {
            Log.e("ResourcesRepository", "Exception getting resource by id", e)
            return@withContext null
        }
    }

    override suspend fun createResource(classroomId: String, input: CreateResource): Resource? = withContext(Dispatchers.IO) {
        try {
            val request = CreateResourceRequestDto(
                name = input.name,
                kindOfResource = input.kindOfResource
            )

            Log.d("ResourcesRepository", "Creating resource for classroom $classroomId with: name=${input.name}, kindOfResource=${input.kindOfResource}")

            val response = service.createResource(
                classroomId = classroomId,
                body = request
            )

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e("ResourcesRepository", "Error creating resource: code=${response.code()}, message=${response.message()}, error=$errorBody")

                // Check if it's a duplicate name error
                if (errorBody?.contains("already exists", ignoreCase = true) == true) {
                    throw DuplicateResourceException(
                        "Resource name '${input.name}' is already taken (globally across all classrooms).\n" +
                        "Try adding a unique identifier, e.g., '${input.name}-${classroomId}' or '${input.name}-Room${classroomId}'"
                    )
                }

                return@withContext null
            }

            val dto = response.body()
            Log.d("ResourcesRepository", "Resource created successfully: $dto")
            return@withContext dto?.toDomain()
        } catch (e: DuplicateResourceException) {
            Log.e("ResourcesRepository", "Duplicate resource name: ${e.message}", e)
            throw e // Re-throw to be caught by ViewModel
        } catch (e: Exception) {
            Log.e("ResourcesRepository", "Exception creating resource: ${e.message}", e)
            return@withContext null
        }
    }

    class DuplicateResourceException(message: String) : Exception(message)

    override suspend fun updateResource(classroomId: String, resourceId: String, input: UpdateResource): Resource? = withContext(Dispatchers.IO) {
        try {
            val request = UpdateResourceRequestDto(
                id = input.id,
                name = input.name,
                kindOfResource = input.kindOfResource,
                classroomId = input.classroomId
            )

            Log.d("ResourcesRepository", "Updating resource $resourceId with: id=${input.id}, name=${input.name}, kindOfResource=${input.kindOfResource}, classroomId=${input.classroomId}")

            val response = service.updateResource(classroomId, resourceId, request)

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e("ResourcesRepository", "Error updating resource: code=${response.code()}, message=${response.message()}, error=$errorBody")
                return@withContext null
            }

            val dto = response.body()
            Log.d("ResourcesRepository", "Resource updated successfully: $dto")
            return@withContext dto?.toDomain()
        } catch (e: Exception) {
            Log.e("ResourcesRepository", "Exception updating resource: ${e.message}", e)
            return@withContext null
        }
    }

    override suspend fun deleteResource(classroomId: String, resourceId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = service.deleteResource(classroomId, resourceId)

            if (!response.isSuccessful) {
                Log.e("ResourcesRepository", "Error deleting resource: ${response.message()}")
                return@withContext false
            }

            return@withContext true
        } catch (e: Exception) {
            Log.e("ResourcesRepository", "Exception deleting resource", e)
            return@withContext false
        }
    }

    private fun ResourceDto.toDomain(): Resource? {
        val id = this.id ?: return null
        val name = this.name ?: return null
        val kindOfResource = this.kindOfResource ?: return null

        // Try to get classroomId from direct field first, then from nested classroom object
        val classroomId = this.classroomId ?: this.classroom?.id ?: run {
            Log.w("ResourcesRepository", "Could not find classroomId in DTO: $this")
            return null
        }

        return Resource(
            id = id,
            name = name,
            kindOfResource = kindOfResource,
            classroomId = classroomId
        )
    }
}
