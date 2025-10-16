package upc.edu.pe.eduspace.features.shared_spaces.data.remote.services

import retrofit2.Response
import retrofit2.http.*
import upc.edu.pe.eduspace.features.shared_spaces.data.remote.models.CreateSharedAreaRequestDto
import upc.edu.pe.eduspace.features.shared_spaces.data.remote.models.SharedAreaDto
import upc.edu.pe.eduspace.features.shared_spaces.data.remote.models.UpdateSharedAreaRequestDto

interface SharedAreasService {
    @GET("shared-area")
    suspend fun getAllSharedAreas(): Response<List<SharedAreaDto>>

    @GET("shared-area/{id}")
    suspend fun getSharedAreaById(@Path("id") id: Int): Response<SharedAreaDto>

    @POST("shared-area")
    suspend fun createSharedArea(@Body request: CreateSharedAreaRequestDto): Response<SharedAreaDto>

    @PUT("shared-area/{id}")
    suspend fun updateSharedArea(
        @Path("id") id: Int,
        @Body request: UpdateSharedAreaRequestDto
    ): Response<SharedAreaDto>

    @DELETE("shared-area/{id}")
    suspend fun deleteSharedArea(@Path("id") id: Int): Response<Unit>
}
