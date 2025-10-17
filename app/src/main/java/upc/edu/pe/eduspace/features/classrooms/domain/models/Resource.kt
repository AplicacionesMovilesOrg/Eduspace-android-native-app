package upc.edu.pe.eduspace.features.classrooms.domain.models

import androidx.annotation.StringRes
import upc.edu.pe.eduspace.R

data class Resource(
    val id: Int,
    val name: String,
    val kindOfResource: String,
    val classroomId: Int
)

enum class ResourceType(@StringRes val displayNameRes: Int, val backendName: String) {
    COMPUTER(R.string.resource_type_computer, "Computer"),
    PROJECTOR(R.string.resource_type_projector, "Projector"),
    DESK(R.string.resource_type_desk, "Desk"),
    WHITEBOARD(R.string.resource_type_whiteboard, "Whiteboard"),
    CHAIR(R.string.resource_type_chair, "Chair"),
    LAPTOP(R.string.resource_type_laptop, "Laptop"),
    TABLET(R.string.resource_type_tablet, "Tablet"),
    PRINTER(R.string.resource_type_printer, "Printer"),
    SCANNER(R.string.resource_type_scanner, "Scanner"),
    MICROPHONE(R.string.resource_type_microphone, "Microphone");

    companion object {
        fun getAllTypes(): List<ResourceType> = entries
        fun fromString(value: String): ResourceType? =
            entries.find { it.backendName.equals(value, ignoreCase = true) }
    }
}
