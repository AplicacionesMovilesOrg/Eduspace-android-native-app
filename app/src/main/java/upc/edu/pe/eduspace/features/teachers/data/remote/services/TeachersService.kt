package upc.edu.pe.eduspace.features.teachers.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import upc.edu.pe.eduspace.features.teachers.data.remote.models.CreateTeacherRequestDto
import upc.edu.pe.eduspace.features.teachers.data.remote.models.TeacherDto
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

interface TeachersService {
    @GET("teachers-profiles")
    suspend fun getAllTeachers(): Response<List<TeacherDto>>

    @POST("teachers-profiles")
    suspend fun createTeacher(@Body body: CreateTeacherRequestDto): Response<TeacherDto>
}