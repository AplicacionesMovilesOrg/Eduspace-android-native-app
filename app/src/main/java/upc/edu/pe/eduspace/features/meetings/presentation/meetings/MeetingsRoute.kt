package upc.edu.pe.eduspace.features.meetings.presentation.meetings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import upc.edu.pe.eduspace.R
import upc.edu.pe.eduspace.core.ui.components.EduSpaceTopAppBar
import upc.edu.pe.eduspace.core.ui.theme.EduGradientBackground
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.presentation.meetings.components.CreateMeetingDialog
import upc.edu.pe.eduspace.features.meetings.presentation.meetings.components.MeetingCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingsRoute(
    drawerState: DrawerState,
    scope: CoroutineScope,
    onNavigateToDetail: (String) -> Unit = {},
    viewModel: MeetingsViewModel = hiltViewModel()
) {
    val meetingsState by viewModel.meetingsState.collectAsStateWithLifecycle()
    val createState by viewModel.createState.collectAsStateWithLifecycle()
    val classroomsState by viewModel.classroomsState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    var showCreateDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedMeeting by remember { mutableStateOf<Meeting?>(null) }
    var snackMessage by remember { mutableStateOf<String?>(null) }

    // Reload data when returning to this screen
    LaunchedEffect(Unit) {
        viewModel.loadMeetings()
        viewModel.getAllClassrooms()
    }

    LaunchedEffect(createState) {
        when (createState) {
            is UiState.Success -> {
                snackMessage = "Meeting created successfully"
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

    MeetingsContent(
        drawerState = drawerState,
        scope = scope,
        meetingsState = meetingsState,
        classroomsState = classroomsState,
        onAddClick = {
            showCreateDialog = true
        },
        onNavigateToDetail = onNavigateToDetail,
        viewModel = viewModel
    )
    if (showCreateDialog) {
        val classrooms = (classroomsState as? UiState.Success)?.data ?: emptyList()
        CreateMeetingDialog(
            classrooms = classrooms,
            onDismiss = { showCreateDialog = false },
            onConfirm = { classroomId, title, description, date, start, end ->
                viewModel.createMeeting(classroomId, title, description, date, start, end)
            }
        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeetingsContent(
    drawerState: DrawerState,
    scope: CoroutineScope,
    meetingsState: UiState<List<Meeting>>,
    classroomsState: UiState<List<Classroom>>,
    onAddClick: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: MeetingsViewModel
) {

    Scaffold(
        topBar = {
            EduSpaceTopAppBar(
                title = stringResource(R.string.nav_meetings),
                drawerState = drawerState,
                scope = scope
            )
        },
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
                    contentDescription = stringResource(R.string.add_meeting),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.padding(12.dp))
                Text(
                    text = stringResource(R.string.add_meeting),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(EduGradientBackground)
        ) {
            when (meetingsState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiState.Success -> {
                    val meetings = (meetingsState as UiState.Success<List<Meeting>>).data
                    if (meetings.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_meetings),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(meetings) { meeting ->
                                MeetingCard(
                                    meeting = meeting,
                                    onClick = {
                                        onNavigateToDetail(meeting.meetingId)
                                    }
                                )
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
                            text = (meetingsState as UiState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadMeetings() }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }

                else -> {}
            }
        }
    }
}