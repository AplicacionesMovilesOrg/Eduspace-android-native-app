package upc.edu.pe.eduspace.features.meetings.presentation.meetings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import upc.edu.pe.eduspace.R
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.meetings.domain.models.Meeting
import upc.edu.pe.eduspace.features.meetings.presentation.meetings.components.MeetingCard

@Composable
fun MeetingsRoute(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: MeetingsViewModel = hiltViewModel()
) {
    val meetingsState by viewModel.meetingsState.collectAsStateWithLifecycle()

    // Reload data when returning to this screen
    LaunchedEffect(Unit) {
        viewModel.loadMeetings()
    }

    Scaffold { paddingValues ->
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
