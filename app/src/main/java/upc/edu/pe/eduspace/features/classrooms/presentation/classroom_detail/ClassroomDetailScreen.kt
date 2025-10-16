package upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom
import upc.edu.pe.eduspace.features.classrooms.domain.models.Resource
import upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components.AddResourceDialog
import upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components.ClassroomInfoCard
import upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components.CustomSnackbar
import upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components.EditResourceDialog
import upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components.EmptyResourcesState
import upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components.ResourceCard
import upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components.ResourcesHeader
import upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components.DeleteConfirmationDialog
import upc.edu.pe.eduspace.features.meetings.presentation.meetings.components.CreateMeetingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomDetailRoute(
    classroomId: Int,
    onNavigateBack: () -> Unit,
    viewModel: ClassroomDetailViewModel = hiltViewModel()
) {
    val classroomState by viewModel.classroomState.collectAsState()
    val teacherState by viewModel.teacherState.collectAsState()
    val resourcesState by viewModel.resourcesState.collectAsState()
    val createResourceState by viewModel.createResourceState.collectAsState()
    val updateResourceState by viewModel.updateResourceState.collectAsState()
    val deleteResourceState by viewModel.deleteResourceState.collectAsState()
    val createMeetingState by viewModel.createMeetingState.collectAsState()

    var showAddResourceDialog by remember { mutableStateOf(false) }
    var showEditResourceDialog by remember { mutableStateOf(false) }
    var showDeleteResourceDialog by remember { mutableStateOf(false) }
    var showCreateMeetingDialog by remember { mutableStateOf(false) }
    var selectedResource by remember { mutableStateOf<Resource?>(null) }
    var snackMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(classroomId) {
        viewModel.getClassroomById(classroomId)
        viewModel.getResourcesByClassroomId(classroomId)
    }

    LaunchedEffect(createResourceState) {
        when (createResourceState) {
            is UiState.Success -> {
                snackMessage = "Resource created successfully"
                showAddResourceDialog = false
                viewModel.resetCreateResourceState()
            }
            is UiState.Error -> {
                snackMessage = (createResourceState as UiState.Error).message
                viewModel.resetCreateResourceState()
            }
            else -> {}
        }
    }

    LaunchedEffect(updateResourceState) {
        when (updateResourceState) {
            is UiState.Success -> {
                snackMessage = "Resource updated successfully"
                showEditResourceDialog = false
                viewModel.resetUpdateResourceState()
            }
            is UiState.Error -> {
                snackMessage = (updateResourceState as UiState.Error).message
                viewModel.resetUpdateResourceState()
            }
            else -> {}
        }
    }

    LaunchedEffect(deleteResourceState) {
        when (deleteResourceState) {
            is UiState.Success -> {
                snackMessage = "Resource deleted successfully"
                showDeleteResourceDialog = false
                viewModel.resetDeleteResourceState()
            }
            is UiState.Error -> {
                snackMessage = (deleteResourceState as UiState.Error).message
                viewModel.resetDeleteResourceState()
            }
            else -> {}
        }
    }

    LaunchedEffect(createMeetingState) {
        when (createMeetingState) {
            is UiState.Success -> {
                snackMessage = "Meeting created successfully"
                showCreateMeetingDialog = false
                viewModel.resetCreateMeetingState()
            }
            is UiState.Error -> {
                snackMessage = (createMeetingState as UiState.Error).message
                viewModel.resetCreateMeetingState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (classroomState) {
                            is UiState.Success -> (classroomState as UiState.Success<Classroom>).data.name
                            else -> "Classroom Detail"
                        },
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
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
            ExtendedFloatingActionButton(
                onClick = { showAddResourceDialog = true },
                containerColor = Color(0xFF2E68B8),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add resource",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Add Resource",
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
            when (classroomState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    val classroom = (classroomState as UiState.Success<Classroom>).data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            ClassroomInfoCard(
                                classroom = classroom,
                                teacherState = teacherState
                            )
                        }

                        item {
                            Button(
                                onClick = { showCreateMeetingDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2E68B8)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Create Meeting for this Classroom",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        item {
                            val resourceCount = when (val state = resourcesState) {
                                is UiState.Success -> state.data.size
                                else -> 0
                            }
                            ResourcesHeader(resourceCount = resourceCount)
                        }

                        when (val state = resourcesState) {
                            is UiState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                            is UiState.Success -> {
                                val resources = state.data
                                if (resources.isEmpty()) {
                                    item {
                                        EmptyResourcesState()
                                    }
                                } else {
                                    items(resources) { resource ->
                                        ResourceCard(
                                            resource = resource,
                                            onEditClick = {
                                                selectedResource = resource
                                                showEditResourceDialog = true
                                            },
                                            onDeleteClick = {
                                                selectedResource = resource
                                                showDeleteResourceDialog = true
                                            }
                                        )
                                    }
                                }
                            }
                            is UiState.Error -> {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = state.message,
                                                color = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = (classroomState as UiState.Error).message,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
                else -> {}
            }
        }
    }

    if (showAddResourceDialog) {
        AddResourceDialog(
            classroomId = classroomId,
            onDismiss = { showAddResourceDialog = false },
            onSubmit = { name, kindOfResource ->
                viewModel.createResource(classroomId, name, kindOfResource)
            }
        )
    }

    if (showEditResourceDialog) {
        selectedResource?.let { resource ->
            EditResourceDialog(
                resource = resource,
                onDismiss = { showEditResourceDialog = false },
                onSubmit = { name, kindOfResource ->
                    viewModel.updateResource(classroomId, resource.id, name, kindOfResource)
                }
            )
        }
    }

    if (showDeleteResourceDialog) {
        selectedResource?.let { resource ->
            DeleteConfirmationDialog(
                classroomName = resource.name,
                onDismiss = { showDeleteResourceDialog = false },
                onConfirm = {
                    viewModel.deleteResource(classroomId, resource.id)
                }
            )
        }
    }

    if (showCreateMeetingDialog) {
        val classroom = (classroomState as? UiState.Success<Classroom>)?.data
        if (classroom != null) {
            CreateMeetingDialog(
                onDismiss = { showCreateMeetingDialog = false },
                onConfirm = { title, description, date, start, end ->
                    // administratorId hardcoded to 1 for now - in production get from auth session
                    viewModel.createMeeting(1, classroom.id, title, description, date, start, end)
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

