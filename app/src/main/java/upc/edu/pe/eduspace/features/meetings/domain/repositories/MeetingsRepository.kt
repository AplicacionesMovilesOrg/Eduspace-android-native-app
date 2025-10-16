package upc.edu.pe.eduspace.features.meetings.domain.repositories

import upc.edu.pe.eduspace.features.meetings.domain.models.CreateMeeting
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.models.UpdateMeeting

interface MeetingsRepository {
    suspend fun getAllMeetings(): List<Meeting>

    suspend fun getMeetingById(id: Int): Meeting?

    suspend fun createMeeting(
        administratorId: Int,
        classroomId: Int,
        meeting: CreateMeeting
    ): Meeting?

    suspend fun updateMeeting(meeting: UpdateMeeting): Meeting?

    suspend fun deleteMeeting(id: Int): Boolean

    suspend fun addTeacherToMeeting(meetingId: Int, teacherId: Int): Boolean
}
