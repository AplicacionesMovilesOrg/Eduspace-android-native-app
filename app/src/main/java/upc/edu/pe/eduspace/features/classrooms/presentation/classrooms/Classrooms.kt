package upc.edu.pe.eduspace.features.classrooms.presentation.classrooms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import upc.edu.pe.eduspace.core.ui.components.CustomSnackbar
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.ClassroomCard
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.ClassroomsHeader
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.CreateClassroomDialog
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.DeleteConfirmationDialog
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.EditClassroomDialog
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.EmptyClassroomsState
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomsRoute(
    onClassroomClick: (Int) -> Unit = {},
    viewModel: ClassroomsViewModel = hiltViewModel()
) {
    val classroomsState by viewModel.classroomsState.collectAsState()
    val teachersState by viewModel.teachersState.collectAsState()
    val createState by viewModel.createState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedClassroom by remember { mutableStateOf<Classroom?>(null) }
    var snackMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAllClassrooms()
        viewModel.getAllTeachers()
    }

    LaunchedEffect(createState) {
        when (createState) {
            is UiState.Success -> {
                snackMessage = "Classroom created successfully"
                showCreateDialog = false
                viewModel.resetCreateState()
            }
            is UiState.Error -> {
                snackMessage = (createState as UiState.Error).message
                viewModel.resetCreateState()
            }
            else -> {}
        }
    }

    LaunchedEffect(updateState) {
        when (updateState) {
            is UiState.Success -> {
                snackMessage = "Classroom updated successfully"
                showEditDialog = false
                viewModel.resetUpdateState()
            }
            is UiState.Error -> {
                snackMessage = (updateState as UiState.Error).message
                viewModel.resetUpdateState()
            }
            else -> {}
        }
    }

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is UiState.Success -> {
                snackMessage = "Classroom deleted successfully"
                showDeleteDialog = false
                viewModel.resetDeleteState()
            }
            is UiState.Error -> {
                snackMessage = (deleteState as UiState.Error).message
                viewModel.resetDeleteState()
            }
            else -> {}
        }
    }

    ClassroomsContent(
        classroomsState = classroomsState,
        teachersState = teachersState,
        onAddClick = { showCreateDialog = true },
        onClassroomClick = onClassroomClick,
        onEditClick = { classroom ->
            selectedClassroom = classroom
            showEditDialog = true
        },
        onDeleteClick = { classroom ->
            selectedClassroom = classroom
            showDeleteDialog = true
        }
    )

    if (showCreateDialog) {
        val teachers = (teachersState as? UiState.Success)?.data ?: emptyList()
        CreateClassroomDialog(
            teachers = teachers,
            onDismiss = { showCreateDialog = false },
            onSubmit = { teacherId, name, description ->
                viewModel.createClassroom(teacherId, name, description)
            }
        )
    }

    if (showEditDialog) {
        selectedClassroom?.let { classroom ->
            val teachers = (teachersState as? UiState.Success)?.data ?: emptyList()
            EditClassroomDialog(
                classroom = classroom,
                teachers = teachers,
                onDismiss = { showEditDialog = false },
                onSubmit = { teacherId, name, description ->
                    viewModel.updateClassroom(classroom.id, teacherId, name, description)
                }
            )
        }
    }

    if (showDeleteDialog) {
        selectedClassroom?.let { classroom ->
            DeleteConfirmationDialog(
                classroomName = classroom.name,
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    viewModel.deleteClassroom(classroom.id)
                }
            )
        }
    }

    snackMessage?.let { msg ->
        CustomSnackbar(
            message = msg,
            onDismiss = { snackMessage = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassroomsContent(
    classroomsState: UiState<List<Classroom>>,
    teachersState: UiState<List<Teacher>>,
    onAddClick: () -> Unit,
    onClassroomClick: (Int) -> Unit,
    onEditClick: (Classroom) -> Unit,
    onDeleteClick: (Classroom) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF2E68B8),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add classroom",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Add Classroom",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
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
            when (classroomsState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    val classrooms = classroomsState.data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            ClassroomsHeader(classroomCount = classrooms.size)
                        }

                        if (classrooms.isEmpty()) {
                            item {
                                EmptyClassroomsState()
                            }
                        } else {
                            items(classrooms) { classroom ->
                                val teachers = (teachersState as? UiState.Success)?.data ?: emptyList()
                                val teacher = teachers.find { it.id == classroom.teacherId }
                                val teacherName = teacher?.let { "${it.firstName} ${it.lastName}" }

                                ClassroomCard(
                                    classroom = classroom,
                                    teacherName = teacherName,
                                    onClick = { onClassroomClick(classroom.id) },
                                    onEditClick = { onEditClick(classroom) },
                                    onDeleteClick = { onDeleteClick(classroom) }
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = classroomsState.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
                else -> {}
            }
        }
    }
}
