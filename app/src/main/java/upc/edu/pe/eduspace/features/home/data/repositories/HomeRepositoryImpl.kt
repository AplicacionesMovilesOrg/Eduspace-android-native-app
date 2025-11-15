package upc.edu.pe.eduspace.features.home.data.repositories

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import upc.edu.pe.eduspace.core.data.SessionManager
import upc.edu.pe.eduspace.features.home.data.remote.models.toDomain
import upc.edu.pe.eduspace.features.home.data.remote.services.HomeService
import upc.edu.pe.eduspace.features.home.domain.models.AdministratorProfile
import upc.edu.pe.eduspace.features.home.domain.models.UserHome
import upc.edu.pe.eduspace.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService,
    private val sessionManager: SessionManager
) : HomeRepository {

    override suspend fun getUserHome(): UserHome {
        Log.d("HomeRepository", "🔵 getUserHome() CALLED")
        val login = sessionManager.userEmailFlow.firstOrNull()
            ?: throw Exception("User not authenticated: login/email not found in session")

        Log.d("HomeRepository", "🔑 Current login: $login")
        val profiles = homeService.getAdministratorProfiles()
        val profileDto = profiles.firstOrNull { dto ->
            dto.email == login
        } ?: throw Exception("Administrator profile not found for login: $login")
        Log.d("HomeRepository", "🟢 Profile found: id=${profileDto.id}, name=${profileDto.firstName}")
        val reports = homeService.getReports().map { it.toDomain() }
        return UserHome(
            firstName = profileDto.firstName,
            lastName = profileDto.lastName,
            reports = reports
        )
    }

    override suspend fun getAdministratorProfiles(): AdministratorProfile {
        return try {
            val login = sessionManager.userEmailFlow.firstOrNull()
                ?: throw Exception("User not authenticated: login/email not found in session")

            Log.d("HomeRepository", "🔑 getAdministratorProfiles() login: $login")

            val profiles = homeService.getAdministratorProfiles()

            val profileDto = profiles.firstOrNull { dto ->
                dto.email == login
            } ?: throw Exception("Administrator profile not found for login: $login")

            Log.d("HomeRepository", "🟢 getAdministratorProfiles() found id=${profileDto.id}")

            profileDto.toDomain()
        } catch (e: Exception) {
            Log.e("HomeRepository", "Error loading administrator profiles: ${e.message}")
            throw e
        }
    }
}
