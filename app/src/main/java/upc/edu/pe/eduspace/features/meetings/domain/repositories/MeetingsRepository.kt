package upc.edu.pe.eduspace.features.meetings.domain.repositories

import upc.edu.pe.eduspace.features.meetings.domain.models.CreateMeeting
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.models.UpdateMeeting

interface MeetingsRepository {
    suspend fun getAllMeetings(): List<Meeting>

    suspend fun getMeetingById(id: String): Meeting?

    suspend fun createMeeting(
        administratorId: String,
        classroomId: String,
        meeting: CreateMeeting
    ): Meeting?

    suspend fun updateMeeting(meeting: UpdateMeeting): Meeting?

    suspend fun deleteMeeting(id: String): Boolean

    suspend fun addTeacherToMeeting(meetingId: String, teacherId: String): Boolean
}
