package upc.edu.pe.eduspace.features.home.domain.models

data class UserHome(
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val reports: List<ReportResource>
)
