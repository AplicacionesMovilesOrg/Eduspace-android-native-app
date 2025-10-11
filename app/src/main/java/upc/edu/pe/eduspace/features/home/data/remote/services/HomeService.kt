package upc.edu.pe.eduspace.features.home.data.remote.services

import retrofit2.http.GET
import upc.edu.pe.eduspace.features.home.data.remote.models.ReportDto

interface HomeService {
    @GET("http://10.0.2.2:8080/api/v1/reports\n")
    suspend fun getReports(): List<ReportDto>
}
