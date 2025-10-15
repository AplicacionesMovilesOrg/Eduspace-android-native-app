package upc.edu.pe.eduspace.features.classrooms.presentation.classrooms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.ClassroomCard
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.CreateClassroomDialog
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.DeleteConfirmationDialog
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.EditClassroomDialog
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomsRoute(
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
        Snackbar(
            action = { TextButton(onClick = { snackMessage = null }) { Text("OK") } },
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
                .padding(16.dp)
        ) { Text(msg) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassroomsContent(
    classroomsState: UiState<List<Classroom>>,
    teachersState: UiState<List<Teacher>>,
    onAddClick: () -> Unit,
    onEditClick: (Classroom) -> Unit,
    onDeleteClick: (Classroom) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF2E68B8),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add classroom")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add classroom")
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
                    if (classrooms.isEmpty()) {
                        Text("No classrooms available", Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(classrooms) { classroom ->
                                val teachers = (teachersState as? UiState.Success)?.data ?: emptyList()
                                val teacher = teachers.find { it.id == classroom.teacherId }
                                val teacherName = teacher?.let { "${it.firstName} ${it.lastName}" }

                                ClassroomCard(
                                    classroom = classroom,
                                    teacherName = teacherName,
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
