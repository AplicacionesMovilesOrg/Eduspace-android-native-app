package upc.edu.pe.eduspace.features.shared_spaces.domain.models

enum class SharedSpaceType(val displayName: String) {
    LIBRARY("Library"),
    CAFETERIA("Cafeteria"),
    STUDY_ROOM("Study Room"),
    AUDITORIUM("Auditorium"),
    LABORATORY("Laboratory"),
    SOCCER_FIELD("Soccer Field"),
    BASKETBALL_COURT("Basketball Court"),
    GYM("Gym"),
    COMPUTER_LAB("Computer Lab"),
    CONFERENCE_ROOM("Conference Room");

    companion object {
        fun fromString(value: String): SharedSpaceType {
            return entries.find { it.name.equals(value, ignoreCase = true) }
                ?: STUDY_ROOM
        }
    }
}
