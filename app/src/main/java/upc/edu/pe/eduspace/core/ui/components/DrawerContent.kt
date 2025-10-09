package upc.edu.pe.eduspace.core.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import upc.edu.pe.eduspace.features.menu.domain.model.MenuEntry
import upc.edu.pe.eduspace.features.menu.domain.model.Screen
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DrawerHeader() {
    // Gradiente con borde redondeado como la imagen
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1FA2FF), Color(0xFF12D8FA), Color(0xFFA6FFCB))
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp))
            .background(gradient)
            .padding(start = 18.dp, top = 28.dp, end = 18.dp, bottom = 8.dp)
    ) {
        Text("Menu", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Divider(color = Color.White.copy(alpha = .7f), thickness = 1.dp)
    }
}

@Composable
fun DrawerMenu(
    items: List<MenuEntry>,
    current: Screen?,
    onClick: (MenuEntry) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        DrawerHeader()
        Spacer(Modifier.height(8.dp))
        items.forEach { item ->
            val color = if (item.isDanger) Color(0xFFDA1E28) else MaterialTheme.colorScheme.onSurface
            val selected = item.screen == current
            NavigationDrawerItem(
                label = { Text(item.screen.label, color = color) },
                selected = selected,
                onClick = { onClick(item) },
                icon = { Icon(item.icon, contentDescription = null, tint = color) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
        Spacer(Modifier.height(12.dp))
        Divider(Modifier.padding(horizontal = 16.dp))
    }
}