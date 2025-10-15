package upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

@Composable
fun ClassroomInfoCard(
    classroom: Classroom,
    teacherState: UiState<Teacher>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0xFF2E68B8).copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header with icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF2E68B8),
                                    Color(0xFF4A90E2)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    "Classroom Information",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Name field with icon
            InfoRowEnhanced(
                icon = Icons.Default.MeetingRoom,
                label = "Name",
                value = classroom.name,
                iconColor = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description field with icon
            InfoRowEnhanced(
                icon = Icons.Default.Description,
                label = "Description",
                value = classroom.description,
                iconColor = Color(0xFF2196F3)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Teacher information with icon
            when (teacherState) {
                is UiState.Success -> {
                    val teacher = (teacherState as UiState.Success<Teacher>).data
                    InfoRowEnhanced(
                        icon = Icons.Default.Person,
                        label = "Teacher",
                        value = "${teacher.firstName} ${teacher.lastName}",
                        iconColor = Color(0xFFFF9800)
                    )
                }
                is UiState.Loading -> {
                    InfoRowEnhanced(
                        icon = Icons.Default.Person,
                        label = "Teacher",
                        value = "Loading...",
                        iconColor = Color(0xFFBDBDBD)
                    )
                }
                is UiState.Error -> {
                    InfoRowEnhanced(
                        icon = Icons.Default.Person,
                        label = "Teacher",
                        value = "Unknown",
                        iconColor = Color(0xFFFF5252)
                    )
                }
                else -> {
                    InfoRowEnhanced(
                        icon = Icons.Default.Person,
                        label = "Teacher",
                        value = "Loading...",
                        iconColor = Color(0xFFBDBDBD)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRowEnhanced(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF757575)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color(0xFF1A1A1A)
            )
        }
    }
}
