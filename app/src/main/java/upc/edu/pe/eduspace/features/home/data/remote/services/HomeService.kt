package upc.edu.pe.eduspace.features.home.data.remote.services

import retrofit2.http.GET
import upc.edu.pe.eduspace.features.home.data.remote.models.ReportDto

interface HomeService {
    @GET("reports") // ← endpoint de tu API
    suspend fun getReports(): List<ReportDto>
}
