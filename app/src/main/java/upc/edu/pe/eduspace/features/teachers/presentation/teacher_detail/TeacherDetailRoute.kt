package upc.edu.pe.eduspace.features.teachers.presentation.teacher_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import upc.edu.pe.eduspace.R
import upc.edu.pe.eduspace.core.ui.theme.EduGradientBackground
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.DeleteConfirmationDialog
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.UpdateTeacher
import upc.edu.pe.eduspace.features.teachers.presentation.teachers.components.EditTeacherDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDetailRoute(
    teacherId: String,
    teacher: Teacher,
    onNavigateBack: () -> Unit,
    onEdit: (String, UpdateTeacher) -> Unit,
    onDelete: (String) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "${teacher.firstName} ${teacher.lastName}",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E68B8)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showEditDialog = true },
                containerColor = Color(0xFF2E68B8),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(EduGradientBackground)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TeacherHeaderCard(teacher)
                }

                item {
                    TeacherInfoCard(teacher)
                }

                item {
                    TeacherContactCard(teacher)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFD32F2F)
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.delete_teacher))
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    if (showEditDialog) {
        EditTeacherDialog(
            teacher = teacher,
            onDismiss = { showEditDialog = false },
            onSubmit = { firstName, lastName, email, dni, address, phone ->
                onEdit(
                    teacherId,
                    UpdateTeacher(firstName, lastName, email, dni, address, phone)
                )
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            title = stringResource(R.string.delete_teacher),
            message = stringResource(
                R.string.delete_teacher_confirm,
                "${teacher.firstName} ${teacher.lastName}"
            ),
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                onDelete(teacherId)
                onNavigateBack()
            }
        )
    }
}

@Composable
private fun TeacherHeaderCard(teacher: Teacher) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2E68B8),
                            Color(0xFF4A90E2)
                        )
                    )
                )
                .padding(vertical = 32.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = (teacher.firstName.firstOrNull()?.toString().orEmpty() +
                            teacher.lastName.firstOrNull()?.toString().orEmpty()).uppercase()
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }

                Text(
                    text = "${teacher.firstName} ${teacher.lastName}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "DNI: ${teacher.dni}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TeacherInfoCard(teacher: Teacher) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Personal Information",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF2E68B8)
            )

            InfoRow(
                icon = Icons.Default.Person,
                label = "Name",
                value = "${teacher.firstName} ${teacher.lastName}"
            )

            InfoRow(
                icon = Icons.Default.Email,
                label = stringResource(R.string.email),
                value = teacher.email
            )
        }
    }
}

@Composable
private fun TeacherContactCard(teacher: Teacher) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Contact Information",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF2E68B8)
            )

            InfoRow(
                icon = Icons.Default.Phone,
                label = stringResource(R.string.teacher_phone),
                value = teacher.phone
            )

            InfoRow(
                icon = Icons.Default.Home,
                label = stringResource(R.string.teacher_address),
                value = teacher.address
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF666666),
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9E9E9E)
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1A1A1A)
            )
        }
    }
}
