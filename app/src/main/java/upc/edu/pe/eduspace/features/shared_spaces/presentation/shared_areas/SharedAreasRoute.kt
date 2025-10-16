package upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import upc.edu.pe.eduspace.core.ui.components.CustomSnackbar
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedArea
import upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas.components.CreateSharedAreaDialog
import upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas.components.DeleteConfirmationDialog
import upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas.components.EditSharedAreaDialog
import upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas.components.SharedAreaCard

@Composable
fun SharedAreasRoute(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: SharedAreasViewModel = hiltViewModel()
) {
    val sharedAreasState by viewModel.sharedAreasState.collectAsStateWithLifecycle()
    val createState by viewModel.createState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedSharedArea by remember { mutableStateOf<SharedArea?>(null) }
    var snackMessage by remember { mutableStateOf<String?>(null) }

    // Reload data when returning to this screen
    LaunchedEffect(Unit) {
        viewModel.loadSharedAreas()
    }

    // Handle create state
    LaunchedEffect(createState) {
        when (createState) {
            is UiState.Success -> {
                snackMessage = "Shared area created successfully"
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

    // Handle update state
    LaunchedEffect(updateState) {
        when (updateState) {
            is UiState.Success -> {
                snackMessage = "Shared area updated successfully"
                showEditDialog = false
                selectedSharedArea = null
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
                snackMessage = "Shared area deleted successfully"
                showDeleteDialog = false
                selectedSharedArea = null
                viewModel.resetDeleteState()
            }
            is UiState.Error -> {
                snackMessage = (deleteState as UiState.Error).message
                viewModel.resetDeleteState()
            }
            else -> {}
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
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
                    contentDescription = "Add",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Add Area",
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
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF7EC0EE), Color(0xFFF5E682))
                    )
                )
        ) {
            when (sharedAreasState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    val sharedAreas = (sharedAreasState as UiState.Success<List<SharedArea>>).data
                    if (sharedAreas.isEmpty()) {
                        Text(
                            text = "No shared areas available",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(sharedAreas) { sharedArea ->
                                SharedAreaCard(
                                    sharedArea = sharedArea,
                                    onEdit = {
                                        selectedSharedArea = sharedArea
                                        showEditDialog = true
                                    },
                                    onDelete = {
                                        selectedSharedArea = sharedArea
                                        showDeleteDialog = true
                                    },
                                    onClick = {
                                        onNavigateToDetail(sharedArea.id)
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
                            text = (sharedAreasState as UiState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadSharedAreas() }) {
                            Text("Retry")
                        }
                    }
                }
                else -> {}
            }
        }
    }

    // Dialogs
    if (showCreateDialog) {
        CreateSharedAreaDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { type, capacity, description ->
                viewModel.createSharedArea(type, capacity, description)
            }
        )
    }

    if (showEditDialog && selectedSharedArea != null) {
        EditSharedAreaDialog(
            sharedArea = selectedSharedArea!!,
            onDismiss = {
                showEditDialog = false
                selectedSharedArea = null
            },
            onConfirm = { type, capacity, description ->
                viewModel.updateSharedArea(
                    selectedSharedArea!!.id,
                    type,
                    capacity,
                    description
                )
            }
        )
    }

    if (showDeleteDialog && selectedSharedArea != null) {
        DeleteConfirmationDialog(
            title = "Delete Shared Area",
            message = "Are you sure you want to delete \"${selectedSharedArea!!.type.displayName}\"?",
            onDismiss = {
                showDeleteDialog = false
                selectedSharedArea = null
            },
            onConfirm = {
                viewModel.deleteSharedArea(selectedSharedArea!!.id)
            }
        )
    }

    snackMessage?.let { msg ->
        CustomSnackbar(
            message = msg,
            onDismiss = { snackMessage = null }
        )
    }
}
