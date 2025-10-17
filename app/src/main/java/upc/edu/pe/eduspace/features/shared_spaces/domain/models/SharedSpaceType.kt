package upc.edu.pe.eduspace.features.shared_spaces.domain.models

import androidx.annotation.StringRes
import upc.edu.pe.eduspace.R

enum class SharedSpaceType(@StringRes val displayNameRes: Int, val backendName: String) {
    LIBRARY(R.string.shared_space_type_library, "Library"),
    CAFETERIA(R.string.shared_space_type_cafeteria, "Cafeteria"),
    STUDY_ROOM(R.string.shared_space_type_study_room, "Study Room"),
    AUDITORIUM(R.string.shared_space_type_auditorium, "Auditorium"),
    LABORATORY(R.string.shared_space_type_laboratory, "Laboratory"),
    SOCCER_FIELD(R.string.shared_space_type_soccer_field, "Soccer Field"),
    BASKETBALL_COURT(R.string.shared_space_type_basketball_court, "Basketball Court"),
    GYM(R.string.shared_space_type_gym, "Gym"),
    COMPUTER_LAB(R.string.shared_space_type_computer_lab, "Computer Lab"),
    CONFERENCE_ROOM(R.string.shared_space_type_conference_room, "Conference Room");

    companion object {
        fun fromString(value: String): SharedSpaceType {
            return entries.find { it.backendName.equals(value, ignoreCase = true) }
                ?: STUDY_ROOM
        }
    }
}
