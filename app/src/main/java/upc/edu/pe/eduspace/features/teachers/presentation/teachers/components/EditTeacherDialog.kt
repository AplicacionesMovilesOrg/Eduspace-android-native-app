package upc.edu.pe.eduspace.features.teachers.presentation.teachers.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import upc.edu.pe.eduspace.R
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

@Composable
fun EditTeacherDialog(
    teacher: Teacher,
    onDismiss: () -> Unit,
    onSubmit: (
        firstName: String,
        lastName: String,
        email: String,
        dni: String,
        address: String,
        phone: String
    ) -> Unit
) {
    var firstName by remember { mutableStateOf(teacher.firstName) }
    var lastName  by remember { mutableStateOf(teacher.lastName) }
    var email     by remember { mutableStateOf(teacher.email) }
    var dni       by remember { mutableStateOf(teacher.dni) }
    var address   by remember { mutableStateOf(teacher.address) }
    var phone     by remember { mutableStateOf(teacher.phone) }

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
                        Icon(Icons.Default.Edit, contentDescription = null, tint = primaryBlue)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.edit_teacher),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = primaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    OutlinedTextField(firstName, { firstName = it }, label = { Text(stringResource(R.string.teacher_first_name)) }, singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(lastName,  { lastName  = it }, label = { Text(stringResource(R.string.last_name_label)) }, singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(email,     { email     = it }, label = { Text(stringResource(R.string.teacher_email)) },   singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(dni,       { dni       = it }, label = { Text(stringResource(R.string.teacher_dni)) },     singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(address,   { address   = it }, label = { Text(stringResource(R.string.teacher_address)) }, singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(phone,     { phone     = it }, label = { Text(stringResource(R.string.teacher_phone)) },  singleLine = true, shape = tfShape, colors = tfColors, modifier = Modifier.fillMaxWidth())

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
                                        address.isNotBlank() && phone.isNotBlank()
                                if (ok) onSubmit(
                                    firstName.trim(), lastName.trim(), email.trim(), dni.trim(),
                                    address.trim(), phone.trim()
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text(stringResource(R.string.update))
                        }
                    }
                }
            }
        }
    }
}