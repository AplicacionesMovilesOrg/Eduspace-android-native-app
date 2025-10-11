package upc.edu.pe.eduspace.features.teachers.data.remote.services

import retrofit2.Response
import retrofit2.http.GET
import upc.edu.pe.eduspace.features.teachers.data.remote.models.TeacherDto

interface TeachersService {
    @GET("teachers-profiles")
    suspend fun getAllTeachers(): Response<List<TeacherDto>>
}