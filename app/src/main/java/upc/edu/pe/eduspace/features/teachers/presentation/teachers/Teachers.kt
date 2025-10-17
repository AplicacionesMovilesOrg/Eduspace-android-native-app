package upc.edu.pe.eduspace.features.teachers.presentation.teachers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import upc.edu.pe.eduspace.R
import upc.edu.pe.eduspace.core.ui.components.CustomSnackbar
import upc.edu.pe.eduspace.core.utils.UiState
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.CreateTeacher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachersRoute(
    viewModel: TeachersViewModel = hiltViewModel(),
) {
    val teachersState by viewModel.teachers.collectAsStateWithLifecycle()
    val createState by viewModel.createState.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    var snack by remember { mutableStateOf<String?>(null) }
    var selectedTeacher by remember { mutableStateOf<Teacher?>(null) }

    val teacherCreatedMessage = stringResource(R.string.teacher_created)

    // Reload data when returning to this screen
    LaunchedEffect(Unit) {
        viewModel.getAllTeachers()
    }

    // Handle create state
    LaunchedEffect(createState) {
        when (createState) {
            is UiState.Success -> {
                snack = teacherCreatedMessage
                showDialog = false
                viewModel.resetCreateState()
            }
            is UiState.Error -> {
                snack = (createState as UiState.Error).message
                viewModel.resetCreateState()
            }
            else -> {}
        }
    }

    TeachersContent(
        teachersState = teachersState,
        onAddTeacher = { showDialog = true },
        onTeacherClick = { selectedTeacher = it },
        onRetry = { viewModel.getAllTeachers() }
    )

    if (showDialog) {
        AddTeacherDialogStyled(
            onDismiss = { showDialog = false },
            onSubmit = { firstName, lastName, email, dni, address, phone, username, password ->
                viewModel.createTeacher(
                    CreateTeacher(
                        firstName = firstName,
                        lastName  = lastName,
                        email     = email,
                        dni       = dni,
                        address   = address,
                        phone     = phone,
                        username  = username,
                        password  = password
                    )
                )
            }
        )
    }

    selectedTeacher?.let { t ->
        TeacherDetailDialog(
            teacher = t,
            onDismiss = { selectedTeacher = null }
        )
    }

    snack?.let { msg ->
        CustomSnackbar(
            message = msg,
            onDismiss = { snack = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeachersContent(
    teachersState: UiState<List<Teacher>>,
    onAddTeacher: () -> Unit = {},
    onTeacherClick: (Teacher) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTeacher,
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
                    contentDescription = stringResource(R.string.add),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    stringResource(R.string.add_teacher),
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
            when (teachersState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    val teachers = teachersState.data
                    if (teachers.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_teachers),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(teachers) { t ->
                                TeacherCard(
                                    t = t,
                                    onClick = { onTeacherClick(t) }
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
                            text = teachersState.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = onRetry) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun TeacherCard(
    t: Teacher,
    onClick: () -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container with gradient
            Box(
                Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF2E68B8),
                                Color(0xFF4A90E2)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                val initials = (t.firstName.firstOrNull()?.toString().orEmpty() +
                        t.lastName.firstOrNull()?.toString().orEmpty()).uppercase()
                Text(
                    initials,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "${t.firstName} ${t.lastName}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    t.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666)
                )
                // DNI badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF2E68B8).copy(alpha = 0.1f)
                ) {
                    Text(
                        "DNI: ${t.dni}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF2E68B8),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun TeacherDetailDialog(
    teacher: Teacher,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                color = Color.White
            ) {
                Column(
                    Modifier
                        .padding(24.dp)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header with gradient icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF2E68B8),
                                            Color(0xFF4A90E2)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            val initials = (teacher.firstName.firstOrNull()?.toString().orEmpty() +
                                    teacher.lastName.firstOrNull()?.toString().orEmpty()).uppercase()
                            Text(
                                initials,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "${teacher.firstName} ${teacher.lastName}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = Color(0xFF2E68B8),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    HorizontalDivider()

                    // Details section
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        DetailRow(label = stringResource(R.string.teacher_email), value = teacher.email)
                        DetailRow(label = stringResource(R.string.teacher_dni), value = teacher.dni)
                        DetailRow(label = stringResource(R.string.teacher_address), value = teacher.address)
                        DetailRow(label = stringResource(R.string.teacher_phone), value = teacher.phone)
                    }

                    // Close button
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E68B8)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.close), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF666666)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF1A1A1A),
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
private fun AddTeacherDialogStyled(
    onDismiss: () -> Unit,
    onSubmit: (
        firstName: String,
        lastName: String,
        email: String,
        dni: String,
        address: String,
        phone: String,
        username: String,
        password: String
    ) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var dni       by remember { mutableStateOf("") }
    var address   by remember { mutableStateOf("") }
    var phone     by remember { mutableStateOf("") }
    var username  by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }

    val primaryBlue = Color(0xFF2E68B8)
    val corner = 24.dp
    val tfShape = RoundedCornerShape(14.dp)
    val tfColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = primaryBlue,
        unfocusedBorderColor = Color(0xFFE0E0E0),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        cursorColor = primaryBlue
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
        ) {
            Surface(
                shape = RoundedCornerShape(corner),
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    Modifier
                        .padding(20.dp)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = primaryBlue)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.add_teacher),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = primaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    OutlinedTextField(firstName, { firstName = it }, label = { Text(stringResource(R.string.teacher_first_name)) }, singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(lastName,  { lastName  = it }, label = { Text(stringResource(R.string.last_name_label)) }, singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(email,     { email     = it }, label = { Text(stringResource(R.string.teacher_email)) },   singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions.Default)
                    OutlinedTextField(dni,       { dni       = it }, label = { Text(stringResource(R.string.teacher_dni)) },     singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(address,   { address   = it }, label = { Text(stringResource(R.string.teacher_address)) }, singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(phone,     { phone     = it }, label = { Text(stringResource(R.string.teacher_phone)) },  singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(username,  { username  = it }, label = { Text(stringResource(R.string.username)) },   singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password)) },
                        singleLine = true,
                        shape = tfShape,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default
                    )

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(stringResource(R.string.cancel), color = primaryBlue)
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val ok = firstName.isNotBlank() && lastName.isNotBlank() &&
                                        email.isNotBlank() && dni.isNotBlank() &&
                                        address.isNotBlank() && phone.isNotBlank() &&
                                        username.isNotBlank() && password.isNotBlank()
                                if (ok) onSubmit(
                                    firstName.trim(), lastName.trim(), email.trim(), dni.trim(),
                                    address.trim(), phone.trim(), username.trim(), password.trim()
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text(stringResource(R.string.add))
                        }
                    }
                }
            }
        }
    }
}