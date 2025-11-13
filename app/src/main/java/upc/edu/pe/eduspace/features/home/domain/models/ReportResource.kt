package upc.edu.pe.eduspace.features.home.domain.models

data class ReportResource(
    val id: String,
    val kindOfReport: String,
    val description: String,
    val resourceId: String,
    val createdAt: String,
    val status: String
)
