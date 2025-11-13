package upc.edu.pe.eduspace.features.home.domain.repositories

import upc.edu.pe.eduspace.features.home.domain.models.AdministratorProfile
import upc.edu.pe.eduspace.features.home.domain.models.UserHome

interface HomeRepository {
    suspend fun getUserHome(): UserHome

    suspend fun getAdministratorProfiles(): AdministratorProfile
}
