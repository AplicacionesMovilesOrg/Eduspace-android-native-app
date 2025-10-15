package upc.edu.pe.eduspace.features.teachers.presentation.teachers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher
import upc.edu.pe.eduspace.features.teachers.domain.repositories.CreateTeacher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachersRoute(
    viewModel: TeachersViewModel = hiltViewModel(),
) {
    val teachers by viewModel.teachers.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var snack by remember { mutableStateOf<String?>(null) }

    var selectedTeacher by remember { mutableStateOf<Teacher?>(null) }

    TeachersContent(
        teachers = teachers,
        onAddTeacher = { showDialog = true },
        onTeacherClick = { selectedTeacher = it }
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
                    ),
                    onDone = {
                        showDialog = false
                        snack = "Created teacher"
                    },
                    onError = { msg -> snack = msg }
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
        Snackbar(
            action = { TextButton(onClick = { snack = null }) { Text("OK") } },
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
                .padding(16.dp)
        ) { Text(msg) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeachersContent(
    teachers: List<Teacher>,
    onAddTeacher: () -> Unit = {},
    onTeacherClick: (Teacher) -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTeacher,
                containerColor = Color(0xFF2E68B8),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add teacher")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add teacher")
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
            if (teachers.isEmpty()) {
                Text("There are no teachers", Modifier.align(Alignment.Center))
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
    }
}

@Composable
private fun TeacherCard(
    t: Teacher,
    onClick: () -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val initials = (t.firstName.firstOrNull()?.toString().orEmpty() +
                    t.lastName.firstOrNull()?.toString().orEmpty()).lowercase()

            Box(
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E6EF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    initials,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF5D6B8A),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "${t.firstName} ${t.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E68B8)
                )
                Text("Email: ${t.email}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "DNI: ${t.dni}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9E9E9E)
                )
            }
        }
    }
}


@Composable
private fun TeacherDetailDialog(
    teacher: Teacher,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "${teacher.firstName} ${teacher.lastName}",
                style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF2E68B8))
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Email: ${teacher.email}", style = MaterialTheme.typography.bodyMedium)
                Text("DNI: ${teacher.dni}", style = MaterialTheme.typography.bodyMedium)
                Text("Address: ${teacher.address}", style = MaterialTheme.typography.bodyMedium)
                Text("Phone: ${teacher.phone}", style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
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
                            "Add teacher",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = primaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    OutlinedTextField(firstName, { firstName = it }, label = { Text("Name") }, singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(lastName,  { lastName  = it }, label = { Text("Last name") }, singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(email,     { email     = it }, label = { Text("Email") },   singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions.Default)
                    OutlinedTextField(dni,       { dni       = it }, label = { Text("DNI") },     singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(address,   { address   = it }, label = { Text("Address") }, singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(phone,     { phone     = it }, label = { Text("Phone") },  singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(username,  { username  = it }, label = { Text("Username") },   singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
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
                            Text("Cancel", color = primaryBlue)
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
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}