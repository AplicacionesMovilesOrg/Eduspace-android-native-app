package upc.edu.pe.eduspace.features.home.data.remote.services

import retrofit2.http.GET
import upc.edu.pe.eduspace.features.home.data.remote.models.ReportDto
import upc.edu.pe.eduspace.features.home.data.remote.models.AdministratorProfileDto
interface HomeService {
    @GET("reports")
    suspend fun getReports(): List<ReportDto>

    @GET("administrator-profiles")
    suspend fun getAdministratorProfiles(): List<AdministratorProfileDto>

}
