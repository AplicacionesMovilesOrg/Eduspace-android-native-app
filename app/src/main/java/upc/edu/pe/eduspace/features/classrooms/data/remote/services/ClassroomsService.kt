package upc.edu.pe.eduspace.features.classrooms.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.ClassroomDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.CreateClassroomRequestDto
import upc.edu.pe.eduspace.features.classrooms.data.remote.models.UpdateClassroomRequestDto

interface ClassroomsService {
    @GET("classrooms")
    suspend fun getAllClassrooms(): Response<List<ClassroomDto>>

    @GET("classrooms/{id}")
    suspend fun getClassroomById(@Path("id") id: String): Response<ClassroomDto>

    @GET("classrooms/teachers/{teacherId}")
    suspend fun getClassroomsByTeacherId(@Path("teacherId") teacherId: String): Response<List<ClassroomDto>>

    @POST("classrooms/teachers/{teacherId}")
    suspend fun createClassroom(
        @Path("teacherId") teacherId: String,
        @Body body: CreateClassroomRequestDto
    ): Response<ClassroomDto>

    @PUT("classrooms/{id}")
    suspend fun updateClassroom(
        @Path("id") id: String,
        @Body body: UpdateClassroomRequestDto
    ): Response<ClassroomDto>

    @DELETE("classrooms/{id}")
    suspend fun deleteClassroom(@Path("id") id: String): Response<Unit>
}
