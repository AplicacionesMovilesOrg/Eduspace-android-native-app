package upc.edu.pe.eduspace.features.menu.data


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import upc.edu.pe.eduspace.features.menu.domain.model.MenuEntry
import upc.edu.pe.eduspace.features.menu.domain.model.Screen
import upc.edu.pe.eduspace.features.menu.domain.repository.MenuRepository

class MenuRepositoryImpl : MenuRepository {
    override fun getMenu(): List<MenuEntry> = listOf(
        MenuEntry(Screen.HOME, Icons.Default.Home),
        MenuEntry(Screen.CLASSROOMS, Icons.Default.MeetingRoom),
        MenuEntry(Screen.SHARED_SPACES, Icons.Default.Group),
        MenuEntry(Screen.MEETINGS, Icons.Default.Event),
        MenuEntry(Screen.TEACHERS, Icons.Default.Person),
        MenuEntry(Screen.LOGOUT, Icons.Default.Logout, isDanger = true)
    )
}