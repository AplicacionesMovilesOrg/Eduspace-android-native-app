package upc.edu.pe.eduspace.features.home.data.remote.models

import upc.edu.pe.eduspace.features.home.domain.models.ReportResource

data class ReportDto(
    val id: Int,
    val kindOfReport: String,
    val description: String,
    val resourceId: Int,
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
