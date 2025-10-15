package upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import upc.edu.pe.eduspace.features.classrooms.domain.models.Resource

@Composable
fun ResourceCard(
    resource: Resource,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val (icon, bgColor, iconColor) = getResourceTypeInfo(resource.kindOfResource)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0xFF2E68B8).copy(alpha = 0.25f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container with gradient background
            Box(
                Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                bgColor,
                                bgColor.copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    resource.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1A1A)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = iconColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        resource.kindOfResource,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = iconColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Action buttons
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF2E68B8).copy(alpha = 0.08f)
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit resource",
                        tint = Color(0xFF2E68B8),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFF5252).copy(alpha = 0.08f)
            ) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete resource",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

private fun getResourceTypeInfo(type: String): Triple<ImageVector, Color, Color> {
    return when (type.lowercase()) {
        "computer", "laptop" -> Triple(
            Icons.Default.Computer,
            Color(0xFFE3F2FD),
            Color(0xFF1976D2)
        )
        "projector" -> Triple(
            Icons.Default.Videocam,
            Color(0xFFFCE4EC),
            Color(0xFFE91E63)
        )
        "desk", "chair" -> Triple(
            Icons.Default.Chair,
            Color(0xFFFFF3E0),
            Color(0xFFFF6F00)
        )
        "whiteboard" -> Triple(
            Icons.Default.Dashboard,
            Color(0xFFF3E5F5),
            Color(0xFF9C27B0)
        )
        "tablet" -> Triple(
            Icons.Default.Tablet,
            Color(0xFFE0F2F1),
            Color(0xFF00796B)
        )
        "printer", "scanner" -> Triple(
            Icons.Default.Print,
            Color(0xFFFFF9C4),
            Color(0xFFF57F17)
        )
        "microphone" -> Triple(
            Icons.Default.Mic,
            Color(0xFFFFEBEE),
            Color(0xFFC62828)
        )
        else -> Triple(
            Icons.Default.Category,
            Color(0xFFECEFF1),
            Color(0xFF455A64)
        )
    }
}
