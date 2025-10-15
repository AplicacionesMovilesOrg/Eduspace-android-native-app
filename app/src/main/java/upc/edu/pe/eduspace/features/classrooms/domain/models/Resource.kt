package upc.edu.pe.eduspace.features.classrooms.domain.models

data class Resource(
    val id: Int,
    val name: String,
    val kindOfResource: String,
    val classroomId: Int
)

enum class ResourceType(val displayName: String) {
    COMPUTER("Computer"),
    PROJECTOR("Projector"),
    DESK("Desk"),
    WHITEBOARD("Whiteboard"),
    CHAIR("Chair"),
    LAPTOP("Laptop"),
    TABLET("Tablet"),
    PRINTER("Printer"),
    SCANNER("Scanner"),
    MICROPHONE("Microphone");

    companion object {
        fun getAllTypes(): List<ResourceType> = entries
        fun fromString(value: String): ResourceType? =
            entries.find { it.displayName.equals(value, ignoreCase = true) }
    }
}
