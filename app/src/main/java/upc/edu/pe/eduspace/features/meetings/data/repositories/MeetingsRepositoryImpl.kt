package upc.edu.pe.eduspace.features.meetings.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import upc.edu.pe.eduspace.features.meetings.data.remote.models.CreateMeetingRequestDto
import upc.edu.pe.eduspace.features.meetings.data.remote.models.UpdateMeetingRequestDto
import upc.edu.pe.eduspace.features.meetings.data.remote.models.toDomain
import upc.edu.pe.eduspace.features.meetings.data.remote.services.MeetingsService
import upc.edu.pe.eduspace.features.meetings.domain.models.CreateMeeting
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.models.UpdateMeeting
import upc.edu.pe.eduspace.features.meetings.domain.repositories.MeetingsRepository
import javax.inject.Inject

class MeetingsRepositoryImpl @Inject constructor(
    private val service: MeetingsService
) : MeetingsRepository {

    override suspend fun getAllMeetings(): List<Meeting> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = service.getAllMeetings()
            if (response.isSuccessful) {
                response.body()?.map { it.toDomain() } ?: emptyList()
            } else {
                Log.e("MeetingsRepository", "Error getting meetings: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MeetingsRepository", "Error getting meetings", e)
            emptyList()
        }
    }

    override suspend fun getMeetingById(id: Int): Meeting? = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = service.getMeetingById(id)
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                Log.e("MeetingsRepository", "Error getting meeting: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("MeetingsRepository", "Error getting meeting", e)
            null
        }
    }

    override suspend fun createMeeting(
        administratorId: Int,
        classroomId: Int,
        meeting: CreateMeeting
    ): Meeting? = withContext(Dispatchers.IO) {
        return@withContext try {
            val dto = CreateMeetingRequestDto(
                title = meeting.title,
                description = meeting.description,
                date = meeting.date,
                start = meeting.start,
                end = meeting.end
            )
            val response = service.createMeeting(administratorId, classroomId, dto)
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("MeetingsRepository", "Error creating meeting: ${response.code()} - $errorBody")
                throw Exception("API error: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("MeetingsRepository", "Error creating meeting", e)
            null
        }
    }

    override suspend fun updateMeeting(meeting: UpdateMeeting): Meeting? = withContext(Dispatchers.IO) {
        return@withContext try {
            val dto = UpdateMeetingRequestDto(
                meetingId = meeting.meetingId,
                title = meeting.title,
                description = meeting.description,
                date = meeting.date,
                start = meeting.start,
                end = meeting.end,
                administratorId = meeting.administratorId,
                classroomId = meeting.classroomId
            )
            val response = service.updateMeeting(meeting.meetingId, dto)
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                Log.e("MeetingsRepository", "Error updating meeting: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("MeetingsRepository", "Error updating meeting", e)
            null
        }
    }

    override suspend fun deleteMeeting(id: Int): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = service.deleteMeeting(id)
            if (response.isSuccessful) {
                true
            } else {
                Log.e("MeetingsRepository", "Error deleting meeting: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("MeetingsRepository", "Error deleting meeting", e)
            false
        }
    }

    override suspend fun addTeacherToMeeting(meetingId: Int, teacherId: Int): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = service.addTeacherToMeeting(meetingId, teacherId)
            if (response.isSuccessful) {
                true
            } else {
                Log.e("MeetingsRepository", "Error adding teacher to meeting: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("MeetingsRepository", "Error adding teacher to meeting", e)
            false
        }
    }
}
