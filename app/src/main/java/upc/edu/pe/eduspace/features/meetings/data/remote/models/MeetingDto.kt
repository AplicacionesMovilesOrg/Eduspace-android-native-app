package upc.edu.pe.eduspace.features.meetings.data.remote.models

import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.models.TeacherInfo

data class MeetingDto(
    val meetingId: Int?,
    val title: String?,
    val description: String?,
    val date: String?,
    val start: String?,
    val end: String?,
    val administrator: AdministratorDto?,
    val classroom: ClassroomRefDto?,
    val teachers: List<TeacherInfoDto>?
)

data class AdministratorDto(
    val administratorId: Int?
)

data class ClassroomRefDto(
    val classroomId: Int?
)

data class TeacherInfoDto(
    val id: Int?,
    val firstName: String?,
    val lastName: String?
)

data class CreateMeetingRequestDto(
    val title: String,
    val description: String,
    val date: String,
    val start: String,
    val end: String
)

data class UpdateMeetingRequestDto(
    val meetingId: Int,
    val title: String,
    val description: String,
    val date: String,
    val start: String,
    val end: String,
    val administratorId: Int,
    val classroomId: Int
)

fun MeetingDto.toDomain(): Meeting {
    return Meeting(
        meetingId = meetingId ?: 0,
        title = title ?: "",
        description = description ?: "",
        date = date ?: "",
        start = start ?: "",
        end = end ?: "",
        administratorId = administrator?.administratorId ?: 0,
        classroomId = classroom?.classroomId ?: 0,
        teachers = teachers?.map { it.toDomain() } ?: emptyList()
    )
}

fun TeacherInfoDto.toDomain(): TeacherInfo {
    return TeacherInfo(
        id = id ?: 0,
        firstName = firstName ?: "",
        lastName = lastName ?: ""
    )
}
