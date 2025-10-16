package upc.edu.pe.eduspace.features.meetings.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import upc.edu.pe.eduspace.features.meetings.data.remote.models.CreateMeetingRequestDto
import upc.edu.pe.eduspace.features.meetings.data.remote.models.MeetingDto
import upc.edu.pe.eduspace.features.meetings.data.remote.models.UpdateMeetingRequestDto

interface MeetingsService {
    @GET("meetings")
    suspend fun getAllMeetings(): Response<List<MeetingDto>>

    @GET("meetings/{id}")
    suspend fun getMeetingById(@Path("id") id: Int): Response<MeetingDto>

    @POST("administrators/{administratorId}/classrooms/{classroomId}/meetings")
    suspend fun createMeeting(
        @Path("administratorId") administratorId: Int,
        @Path("classroomId") classroomId: Int,
        @Body meeting: CreateMeetingRequestDto
    ): Response<MeetingDto>

    @PUT("meetings/{id}")
    suspend fun updateMeeting(
        @Path("id") id: Int,
        @Body meeting: UpdateMeetingRequestDto
    ): Response<MeetingDto>

    @DELETE("meetings/{id}")
    suspend fun deleteMeeting(@Path("id") id: Int): Response<Void>

    @POST("meetings/{meetingId}/teachers/{teacherId}")
    suspend fun addTeacherToMeeting(
        @Path("meetingId") meetingId: Int,
        @Path("teacherId") teacherId: Int
    ): Response<Void>
}
