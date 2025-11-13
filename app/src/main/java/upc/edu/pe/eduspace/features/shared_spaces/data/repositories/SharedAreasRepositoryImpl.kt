package upc.edu.pe.eduspace.features.shared_spaces.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import upc.edu.pe.eduspace.features.shared_spaces.data.remote.models.CreateSharedAreaRequestDto
import upc.edu.pe.eduspace.features.shared_spaces.data.remote.models.UpdateSharedAreaRequestDto
import upc.edu.pe.eduspace.features.shared_spaces.data.remote.services.SharedAreasService
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.CreateSharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.UpdateSharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.repositories.SharedAreasRepository
import javax.inject.Inject

class SharedAreasRepositoryImpl @Inject constructor(
    private val sharedAreasService: SharedAreasService
) : SharedAreasRepository {

    override suspend fun getAllSharedAreas(): List<SharedArea> = withContext(Dispatchers.IO) {
        try {
            val response = sharedAreasService.getAllSharedAreas()
            if (response.isSuccessful) {
                response.body()?.map { it.toDomain() } ?: emptyList()
            } else {
                Log.e("SharedAreasRepository", "Error getting shared areas: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("SharedAreasRepository", "Exception getting shared areas", e)
            emptyList()
        }
    }

    override suspend fun getSharedAreaById(id: String): SharedArea? = withContext(Dispatchers.IO) {
        try {
            val response = sharedAreasService.getSharedAreaById(id)
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                Log.e("SharedAreasRepository", "Error getting shared area: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("SharedAreasRepository", "Exception getting shared area", e)
            null
        }
    }

    override suspend fun createSharedArea(sharedArea: CreateSharedArea): SharedArea? =
        withContext(Dispatchers.IO) {
            try {
                val request = CreateSharedAreaRequestDto(
                    name = sharedArea.type.name,
                    capacity = sharedArea.capacity,
                    description = sharedArea.description
                )
                val response = sharedAreasService.createSharedArea(request)
                if (response.isSuccessful) {
                    response.body()?.toDomain()
                } else {
                    Log.e("SharedAreasRepository", "Error creating shared area: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("SharedAreasRepository", "Exception creating shared area", e)
                null
            }
        }

    override suspend fun updateSharedArea(sharedArea: UpdateSharedArea): SharedArea? =
        withContext(Dispatchers.IO) {
            try {
                val request = UpdateSharedAreaRequestDto(
                    id = sharedArea.id,
                    name = sharedArea.type.name,
                    capacity = sharedArea.capacity,
                    description = sharedArea.description
                )
                val response = sharedAreasService.updateSharedArea(sharedArea.id, request)
                if (response.isSuccessful) {
                    response.body()?.toDomain()
                } else {
                    Log.e("SharedAreasRepository", "Error updating shared area: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("SharedAreasRepository", "Exception updating shared area", e)
                null
            }
        }

    override suspend fun deleteSharedArea(id: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = sharedAreasService.deleteSharedArea(id)
            if (response.isSuccessful) {
                true
            } else {
                Log.e("SharedAreasRepository", "Error deleting shared area: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("SharedAreasRepository", "Exception deleting shared area", e)
            false
        }
    }
}
