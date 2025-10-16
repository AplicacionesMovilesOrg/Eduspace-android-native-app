package upc.edu.pe.eduspace.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import upc.edu.pe.eduspace.R
import upc.edu.pe.eduspace.core.data.LanguagePreferences
import upc.edu.pe.eduspace.core.utils.getLocalizedLabel
import upc.edu.pe.eduspace.features.menu.domain.model.MenuEntry
import upc.edu.pe.eduspace.features.menu.domain.model.Screen

@Composable
fun DrawerHeader() {
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
        Text(stringResource(R.string.menu), color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(
            color = Color.White.copy(alpha = .7f),
            thickness = 1.dp
        )
    }
}

@Composable
fun DrawerMenu(
    items: List<MenuEntry>,
    current: Screen?,
    onClick: (MenuEntry) -> Unit,
    languagePreferences: LanguagePreferences,
    currentLanguage: String
) {
    Column(Modifier.fillMaxSize()) {
        // Menu items
        Column(Modifier.weight(1f)) {
            DrawerHeader()
            Spacer(Modifier.height(8.dp))
            items.forEach { item ->
                val selected = item.screen == current

                val primaryCyan = Color(0xFF1FA2FF)
                val textColorDark = Color(0xFF161616)

                NavigationDrawerItem(
                    label = { Text(item.screen.getLocalizedLabel()) },
                    selected = selected,
                    onClick = { onClick(item) },
                    icon = { Icon(item.icon, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = if (item.isDanger) Color(0xFFDA1E28) else textColorDark,
                        unselectedIconColor = if (item.isDanger) Color(0xFFDA1E28) else textColorDark,
                        selectedTextColor = if (item.isDanger) Color(0xFFDA1E28) else primaryCyan,
                        selectedIconColor = if (item.isDanger) Color(0xFFDA1E28) else primaryCyan,
                        selectedContainerColor = primaryCyan.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }

        // Language selector at bottom
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))
        LanguageSelector(
            languagePreferences = languagePreferences,
            currentLanguage = currentLanguage
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun LanguageSelector(
    languagePreferences: LanguagePreferences,
    currentLanguage: String
) {
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1FA2FF).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = stringResource(R.string.language),
                    tint = Color(0xFF1FA2FF),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    text = stringResource(R.string.language),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF161616)
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // English button
                IconButton(
                    onClick = {
                        scope.launch {
                            languagePreferences.setLanguage("en")
                        }
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Text(
                        text = "🇺🇸",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .background(
                                color = if (currentLanguage == "en") Color(0xFF1FA2FF).copy(alpha = 0.2f)
                                       else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp)
                    )
                }

                Spacer(Modifier.padding(4.dp))

                // Spanish button
                IconButton(
                    onClick = {
                        scope.launch {
                            languagePreferences.setLanguage("es")
                        }
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Text(
                        text = "🇪🇸",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .background(
                                color = if (currentLanguage == "es") Color(0xFF1FA2FF).copy(alpha = 0.2f)
                                       else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}