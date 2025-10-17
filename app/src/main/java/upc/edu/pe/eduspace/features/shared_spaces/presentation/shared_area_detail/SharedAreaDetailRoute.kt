package upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_area_detail

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
import androidx.compose.material.icons.filled.Group
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
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedArea
import upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas.components.DeleteConfirmationDialog
import upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas.components.EditSharedAreaDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedAreaDetailRoute(
    onNavigateBack: () -> Unit,
    viewModel: SharedAreaDetailViewModel = hiltViewModel()
) {
    val sharedAreaState by viewModel.sharedAreaState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var snackMessage by remember { mutableStateOf<String?>(null) }

    val sharedSpaceUpdatedMessage = stringResource(R.string.shared_space_updated)
    val sharedSpaceDeletedMessage = stringResource(R.string.shared_space_deleted)

    // Handle update state
    LaunchedEffect(updateState) {
        when (updateState) {
            is UiState.Success -> {
                snackMessage = sharedSpaceUpdatedMessage
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
                snackMessage = sharedSpaceDeletedMessage
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (sharedAreaState) {
                            is UiState.Success -> stringResource((sharedAreaState as UiState.Success<SharedArea>).data.type.displayNameRes)
                            else -> stringResource(R.string.shared_space_detail)
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
            when (sharedAreaState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    val sharedArea = (sharedAreaState as UiState.Success<SharedArea>).data
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
                                Text(
                                    text = stringResource(sharedArea.type.displayNameRes),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                HorizontalDivider()

                                // Capacity
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = stringResource(R.string.capacity),
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = stringResource(R.string.capacity),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "${sharedArea.capacity} people",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                HorizontalDivider()

                                // Description
                                Column {
                                    Text(
                                        text = stringResource(R.string.shared_space_description),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = sharedArea.description.ifEmpty { stringResource(R.string.no_data_available) },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
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
                                Text(stringResource(R.string.edit), fontWeight = FontWeight.Bold)
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
                                Text(stringResource(R.string.delete), fontWeight = FontWeight.Bold)
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
                            text = (sharedAreaState as UiState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadSharedArea() }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                else -> {}
            }
        }
    }

    // Dialogs
    if (showEditDialog) {
        val sharedArea = (sharedAreaState as? UiState.Success<SharedArea>)?.data
        if (sharedArea != null) {
            EditSharedAreaDialog(
                sharedArea = sharedArea,
                onDismiss = { showEditDialog = false },
                onConfirm = { type, capacity, description ->
                    viewModel.updateSharedArea(type, capacity, description)
                }
            )
        }
    }

    if (showDeleteDialog) {
        val sharedArea = (sharedAreaState as? UiState.Success<SharedArea>)?.data
        if (sharedArea != null) {
            DeleteConfirmationDialog(
                title = stringResource(R.string.delete_shared_space),
                message = stringResource(R.string.delete_shared_space_confirm, stringResource(sharedArea.type.displayNameRes)),
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    viewModel.deleteSharedArea()
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
