package upc.edu.pe.eduspace.features.home.data.remote.repositories

import upc.edu.pe.eduspace.features.home.data.remote.services.HomeService
import upc.edu.pe.eduspace.features.home.data.remote.models.toDomain
import upc.edu.pe.eduspace.features.home.domain.models.ReportResource
import upc.edu.pe.eduspace.features.home.domain.models.UserHome
import upc.edu.pe.eduspace.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService
) : HomeRepository {

    override suspend fun getUserHome(): UserHome {
        val reports = homeService.getReports().map { it.toDomain() }

        // Por ahora el nombre del usuario lo podrías obtener del token o de authRepo
        return UserHome(
            firstName = "Andres",
            lastName = "Torres",
            reports = reports
        )
    }
}
