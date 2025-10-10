package upc.edu.pe.eduspace.features.home.data.repositories

import kotlinx.coroutines.delay
import upc.edu.pe.eduspace.features.home.domain.models.UserHome
import upc.edu.pe.eduspace.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor() : HomeRepository {
    override suspend fun getUserHome(): UserHome {
        delay(1000) // simular llamada remota
        return UserHome(
            firstName = "Andres",
            lastName = "Torres",
            hasReports = false
        )
    }
}
