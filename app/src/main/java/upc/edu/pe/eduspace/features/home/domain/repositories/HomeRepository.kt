package upc.edu.pe.eduspace.features.home.domain.repositories

import upc.edu.pe.eduspace.features.home.domain.models.UserHome

interface HomeRepository {
    suspend fun getUserHome(): UserHome
}
