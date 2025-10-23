package upc.edu.pe.eduspace.features.meetings.data.remote.models

import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.domain.models.TeacherInfo

data class MeetingDto(
    val meetingId: String?,
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
    val administratorId: String?
)

data class ClassroomRefDto(
    val classroomId: String?
)

data class TeacherInfoDto(
    val id: String?,
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
    val meetingId: String,
    val title: String,
    val description: String,
    val date: String,
    val start: String,
    val end: String,
    val administratorId: String,
    val classroomId: String
)

fun MeetingDto.toDomain(): Meeting {
    return Meeting(
        meetingId = meetingId ?: "",
        title = title ?: "",
        description = description ?: "",
        date = date ?: "",
        start = start ?: "",
        end = end ?: "",
        administratorId = administrator?.administratorId ?: "",
        classroomId = classroom?.classroomId ?: "",
        teachers = teachers?.map { it.toDomain() } ?: emptyList()
    )
}

fun TeacherInfoDto.toDomain(): TeacherInfo {
    return TeacherInfo(
        id = id ?: "",
        firstName = firstName ?: "",
        lastName = lastName ?: ""
    )
}
