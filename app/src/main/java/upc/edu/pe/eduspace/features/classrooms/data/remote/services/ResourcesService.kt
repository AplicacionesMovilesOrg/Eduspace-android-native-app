package upc.edu.pe.eduspace.features.classrooms.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.CreateResourceRequestDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.ResourceDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.UpdateResourceRequestDto

interface ResourcesService {
    @POST("classrooms/{classroomId}/resources")
    suspend fun createResource(
        @Path("classroomId") classroomId: String,
        @Body body: CreateResourceRequestDto
    ): Response<ResourceDto>

    @GET("classrooms/{classroomId}/resources")
    suspend fun getResourcesByClassroomId(
        @Path("classroomId") classroomId: String
    ): Response<List<ResourceDto>>

    @GET("classrooms/{classroomId}/resources/{resourceId}")
    suspend fun getResourceById(
        @Path("classroomId") classroomId: String,
        @Path("resourceId") resourceId: String
    ): Response<ResourceDto>

    @PUT("classrooms/{classroomId}/resources/{resourceId}")
    suspend fun updateResource(
        @Path("classroomId") classroomId: String,
        @Path("resourceId") resourceId: String,
        @Body body: UpdateResourceRequestDto
    ): Response<ResourceDto>

    @DELETE("classrooms/{classroomId}/resources/{resourceId}")
    suspend fun deleteResource(
        @Path("classroomId") classroomId: String,
        @Path("resourceId") resourceId: String
    ): Response<Unit>
}
