package upc.edu.pe.eduspace.features.shared_spaces.domain.repositories

import upc.edu.pe.eduspace.features.shared_spaces.domain.models.CreateSharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.UpdateSharedArea

interface SharedAreasRepository {
    suspend fun getAllSharedAreas(): List<SharedArea>
    suspend fun getSharedAreaById(id: Int): SharedArea?
    suspend fun createSharedArea(sharedArea: CreateSharedArea): SharedArea?
    suspend fun updateSharedArea(sharedArea: UpdateSharedArea): SharedArea?
    suspend fun deleteSharedArea(id: Int): Boolean
}
