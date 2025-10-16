package upc.edu.pe.eduspace.features.home.data.repositories

import android.util.Log
import upc.edu.pe.eduspace.features.home.data.remote.models.toDomain
import upc.edu.pe.eduspace.features.home.data.remote.services.HomeService
import upc.edu.pe.eduspace.features.home.domain.models.UserHome
import upc.edu.pe.eduspace.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService
) : HomeRepository {

    override suspend fun getUserHome(): UserHome {
        try {
            val profiles = homeService.getAdministratorProfiles()
            val profile = profiles.firstOrNull()

            val reports = homeService.getReports().map { it.toDomain() }

            return UserHome(
                firstName = profile?.firstName ?: "User",
                lastName = profile?.lastName ?: "",
                reports = reports
            )
        } catch (e: Exception) {
            Log.e("HomeRepository", "Error loading home data: ${e.message}")
            throw e
        }
    }
}
