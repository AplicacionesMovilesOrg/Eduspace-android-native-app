package upc.edu.pe.eduspace.features.menu.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuEntry(
    val screen: Screen,
    val icon: ImageVector,
    val isDanger: Boolean = false
)