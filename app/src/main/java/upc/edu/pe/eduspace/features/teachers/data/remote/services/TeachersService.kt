package upc.edu.pe.eduspace.features.teachers.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import upc.edu.pe.eduspace.features.teachers.data.remote.models.CreateTeacherRequestDto
import upc.edu.pe.eduspace.features.teachers.data.remote.models.TeacherDto
import upc.edu.pe.eduspace.features.teachers.data.remote.models.UpdateTeacherRequestDto

interface TeachersService {
    @GET("teachers-profiles")
    suspend fun getAllTeachers(): Response<List<TeacherDto>>

    @GET("teachers-profiles/{id}")
    suspend fun getTeacherById(@Path("id") id: String): Response<TeacherDto>

    @POST("teachers-profiles")
    suspend fun createTeacher(@Body body: CreateTeacherRequestDto): Response<TeacherDto>

    @PUT("teachers-profiles/{id}")
    suspend fun updateTeacher(@Path("id") id: String, @Body body: UpdateTeacherRequestDto): Response<TeacherDto>

    @DELETE("teachers-profiles/{id}")
    suspend fun deleteTeacher(@Path("id") id: String): Response<Unit>
}