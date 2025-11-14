package upc.edu.pe.eduspace.features.home.data.repositories

import android.util.Log
import kotlinx.coroutines.flow.first
import upc.edu.pe.eduspace.core.data.SessionManager
import upc.edu.pe.eduspace.features.home.data.remote.models.toDomain
import upc.edu.pe.eduspace.features.home.data.remote.services.HomeService
import upc.edu.pe.eduspace.features.home.domain.models.UserHome
import upc.edu.pe.eduspace.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService,
    private val sessionManager: SessionManager
) : HomeRepository {

    override suspend fun getUserHome(): UserHome {
        try {

            val emailFromSession = sessionManager.userEmailFlow.first()


            val profiles = homeService.getAdministratorProfiles()
            val profile = profiles.firstOrNull { it.email.equals(emailFromSession, ignoreCase = true) }


            val reports = homeService.getReports().map { it.toDomain() }

            val firstName = profile?.firstName ?: "User"
            val lastName = profile?.lastName ?: ""
            val displayName = "$firstName $lastName".trim()

            return UserHome(
                firstName = firstName,
                lastName = lastName,
                reports = reports,
                displayName = displayName
            )
        } catch (e: Exception) {
            Log.e("HomeRepository", "Error loading home data: ${e.message}")
            throw e
        }
    }
}
