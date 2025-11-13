package upc.edu.pe.eduspace.features.home.data.remote.models

import upc.edu.pe.eduspace.features.home.domain.models.ReportResource

data class ReportDto(
    val id: String,
    val kindOfReport: String,
    val description: String,
    val resourceId: String,
    val createdAt: String,
    val status: String
)

fun ReportDto.toDomain() = ReportResource(
    id = id,
    kindOfReport = kindOfReport,
    description = description,
    resourceId = resourceId,
    createdAt = createdAt,
    status = status
)
