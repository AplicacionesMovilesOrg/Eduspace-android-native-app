package upc.edu.pe.eduspace.features.meetings.domain.models

data class Meeting(
    val meetingId: String,
    val title: String,
    val description: String,
    val date: String,
    val start: String,
    val end: String,
    val administratorId: String,
    val classroomId: String,
    val teachers: List<TeacherInfo>
)

data class TeacherInfo(
    val id: String,
    val firstName: String,
    val lastName: String
)

data class CreateMeeting(
    val title: String,
    val description: String,
    val date: String,
    val start: String,
    val end: String
)

data class UpdateMeeting(
    val meetingId: String,
    val title: String,
    val description: String,
    val date: String,
    val start: String,
    val end: String,
    val administratorId: String,
    val classroomId: String
)
