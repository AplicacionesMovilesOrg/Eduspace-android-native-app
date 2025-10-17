package upc.edu.pe.eduspace.features.meetings.presentation.meeting_detail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import upc.edu.pe.eduspace.R
import upc.edu.pe.eduspace.core.ui.components.CustomSnackbar
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.presentation.meetings.components.AddTeacherDialog
import upc.edu.pe.eduspace.features.meetings.presentation.meetings.components.DeleteConfirmationDialog
import upc.edu.pe.eduspace.features.meetings.presentation.meetings.components.EditMeetingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingDetailRoute(
    onNavigateBack: () -> Unit,
    viewModel: MeetingDetailViewModel = hiltViewModel()
) {
    val meetingState by viewModel.meetingState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()
    val addTeacherState by viewModel.addTeacherState.collectAsStateWithLifecycle()
    val availableTeachersState by viewModel.availableTeachers.collectAsStateWithLifecycle()

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddTeacherDialog by remember { mutableStateOf(false) }
    var snackMessage by remember { mutableStateOf<String?>(null) }

    val meetingUpdatedMsg = stringResource(R.string.meeting_updated)
    val meetingDeletedMsg = stringResource(R.string.meeting_deleted)
    val teacherAddedMsg = stringResource(R.string.teacher_added)
    val noTeachersAvailableMsg = stringResource(id = R.string.no_teachers_available)

    // Handle update state
    LaunchedEffect(updateState) {
        when (updateState) {
            is UiState.Success -> {
                snackMessage = meetingUpdatedMsg
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

    // Handle delete state
    LaunchedEffect(deleteState) {
        when (deleteState) {
            is UiState.Success -> {
                snackMessage = meetingDeletedMsg
                viewModel.resetDeleteState()
                onNavigateBack()
            }
            is UiState.Error -> {
                snackMessage = (deleteState as UiState.Error).message
                viewModel.resetDeleteState()
            }
            else -> {}
        }
    }

    // Handle add teacher state
    LaunchedEffect(addTeacherState) {
        when (addTeacherState) {
            is UiState.Success -> {
                snackMessage = teacherAddedMsg
                showAddTeacherDialog = false
                viewModel.resetAddTeacherState()
            }
            is UiState.Error -> {
                snackMessage = (addTeacherState as UiState.Error).message
                viewModel.resetAddTeacherState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (meetingState) {
                            is UiState.Success -> (meetingState as UiState.Success<Meeting>).data.title
                            else -> stringResource(R.string.meeting_details)
                        },
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF7EC0EE), Color(0xFFF5E682))
                    )
                )
        ) {
            when (meetingState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    val meeting = (meetingState as UiState.Success<Meeting>).data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Main Information Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Event,
                                        contentDescription = null,
                                        tint = Color(0xFF2E68B8),
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = meeting.title,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                HorizontalDivider()

                                // Description
                                Column {
                                    Text(
                                        text = stringResource(R.string.description),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = meeting.description.ifEmpty { stringResource(R.string.no_description) },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                HorizontalDivider()

                                // Date & Time
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stringResource(R.string.date),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF4CAF50).copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = meeting.date,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Medium
                                                ),
                                                color = Color(0xFF4CAF50),
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stringResource(R.string.time),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF2E68B8).copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = "${meeting.start} - ${meeting.end}",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Medium
                                                ),
                                                color = Color(0xFF2E68B8),
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Participants Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.participants, meeting.teachers.size),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Button(
                                        onClick = {
                                            // Check if there are available teachers before opening dialog
                                            val state = availableTeachersState
                                            if (state is UiState.Success) {
                                                if (state.data.isEmpty()) {
                                                    // If the filtered list is empty, show snackbar
                                                    snackMessage = noTeachersAvailableMsg
                                                } else {
                                                    // If there are available teachers, show the dialog
                                                    showAddTeacherDialog = true
                                                }
                                            } else {
                                                // If still loading or error, allow opening dialog (it will handle states)
                                                showAddTeacherDialog = true
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF2E68B8)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.PersonAdd,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(stringResource(id = R.string.add), style = MaterialTheme.typography.labelMedium)
                                    }
                                }

                                if (meeting.teachers.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.no_participants_yet),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF666666)
                                    )
                                } else {
                                    meeting.teachers.forEach { teacher ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF2E68B8).copy(alpha = 0.05f)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .background(
                                                            Brush.linearGradient(
                                                                colors = listOf(
                                                                    Color(0xFF2E68B8),
                                                                    Color(0xFF4A90E2)
                                                                )
                                                            ),
                                                            shape = RoundedCornerShape(8.dp)
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    val initials = (teacher.firstName.firstOrNull()?.toString().orEmpty() +
                                                            teacher.lastName.firstOrNull()?.toString().orEmpty()).uppercase()
                                                    Text(
                                                        text = initials,
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(12.dp))

                                                Text(
                                                    text = "${teacher.firstName} ${teacher.lastName}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { showEditDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2E68B8)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(stringResource(id = R.string.edit), fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { showDeleteDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF5252)
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(stringResource(id = R.string.delete), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = (meetingState as UiState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadMeeting() }) {
                            Text(stringResource(id = R.string.retry))
                        }
                    }
                }
                else -> {}
            }
        }
    }

    // Dialogs
    if (showEditDialog) {
        val meeting = (meetingState as? UiState.Success<Meeting>)?.data
        if (meeting != null) {
            EditMeetingDialog(
                meeting = meeting,
                onDismiss = { showEditDialog = false },
                onConfirm = { title, description, date, start, end ->
                    viewModel.updateMeeting(title, description, date, start, end)
                }
            )
        }
    }

    if (showDeleteDialog) {
        val meeting = (meetingState as? UiState.Success<Meeting>)?.data
        if (meeting != null) {
            DeleteConfirmationDialog(
                title = stringResource(id = R.string.delete_meeting_title),
                message = stringResource(id = R.string.delete_meeting_message, meeting.title),
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    viewModel.deleteMeeting()
                }
            )
        }
    }

    if (showAddTeacherDialog) {
        // Handle the state of available teachers
        when (val state = availableTeachersState) {
            is UiState.Loading -> {
                // Optionally show a loading indicator in the dialog
                AddTeacherDialog(
                    teachers = emptyList(),
                    onDismiss = { showAddTeacherDialog = false },
                    onConfirm = { teacherId ->
                        viewModel.addTeacherToMeeting(teacherId)
                    }
                )
            }
            is UiState.Error -> {
                // Show error if teachers failed to load
                LaunchedEffect(state) {
                    snackMessage = state.message
                    showAddTeacherDialog = false
                }
            }
            is UiState.Success -> {
                // Pass the FILTERED list to the dialog
                AddTeacherDialog(
                    teachers = state.data,
                    onDismiss = { showAddTeacherDialog = false },
                    onConfirm = { teacherId ->
                        viewModel.addTeacherToMeeting(teacherId)
                    }
                )
            }
            else -> {}
        }
    }

    snackMessage?.let { msg ->
        CustomSnackbar(
            message = msg,
            onDismiss = { snackMessage = null }
        )
    }
}
