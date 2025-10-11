package upc.edu.pe.eduspace.features.teachers.presentation.teachers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachersRoute(
    viewModel: TeachersViewModel = hiltViewModel(),
    onAddTeacher: () -> Unit = {}
) {
    val teachers by viewModel.teachers.collectAsState()
    TeachersContent(
        teachers = teachers,
        onAddTeacher = onAddTeacher
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeachersContent(
    teachers: List<Teacher>,
    onAddTeacher: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Teacher Management") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E68B8),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTeacher,
                containerColor = Color(0xFF2E68B8),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add teacher"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add teacher")
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF7EC0EE), Color(0xFFF5E682))
                    )
                )
        ) {
            if (teachers.isEmpty()) {
                Text("There are no teachers", Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(teachers) { t -> TeacherCard(t) }
                }
            }
        }
    }
}

@Composable
private fun TeacherCard(t: Teacher) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val initials = (t.firstName.firstOrNull()?.toString().orEmpty() +
                    t.lastName.firstOrNull()?.toString().orEmpty()).lowercase()

            Box(
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E6EF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    initials,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF5D6B8A),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "${t.firstName} ${t.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E68B8)
                )
                Text("Email: ${t.email}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "DNI: ${t.dni}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9E9E9E)
                )
            }
        }
    }
}


//SOLO PARA TESTEO
@Preview(showBackground = true, name = "Teachers – Lista")
@Composable
private fun TeachersPreview_List() {
    val fake = listOf(
        Teacher(firstName = "Ana",  lastName = "Pérez", email = "ana@upc.edu.pe",  dni = "12345678"),
        Teacher(firstName = "Luis", lastName = "Gómez", email = "luis@upc.edu.pe", dni = "87654321")
    )
    TeachersContent(teachers = fake)
}
