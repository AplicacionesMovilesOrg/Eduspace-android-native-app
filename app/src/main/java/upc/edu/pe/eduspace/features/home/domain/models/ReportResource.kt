package upc.edu.pe.eduspace.features.home.domain.models

data class ReportResource(
    val id: Int,
    val kindOfReport: String,
    val description: String,
    val resourceId: Int,
    val createdAt: String,
    val status: String
)
