package upc.edu.pe.eduspace.features.classrooms.presentation.classrooms.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import upc.edu.pe.eduspace.R
import upc.edu.pe.eduspace.features.classrooms.domain.models.Classroom
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClassroomDialog(
    classroom: Classroom,
    teachers: List<Teacher>,
    onDismiss: () -> Unit,
    onSubmit: (teacherId: Int, name: String, description: String) -> Unit
) {
    val initialTeacher = teachers.find { it.id == classroom.teacherId }
    var selectedTeacher by remember { mutableStateOf(initialTeacher) }
    var expanded by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(classroom.name) }
    var description by remember { mutableStateOf(classroom.description) }

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
                            stringResource(R.string.edit_classroom),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = primaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedTeacher?.let { "${it.firstName} ${it.lastName}" } ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.select_teacher)) },
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            shape = tfShape,
                            colors = tfColors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            teachers.forEach { teacher ->
                                DropdownMenuItem(
                                    text = { Text("${teacher.firstName} ${teacher.lastName}") },
                                    onClick = {
                                        selectedTeacher = teacher
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(R.string.classroom_name)) },
                        singleLine = true,
                        shape = tfShape,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(stringResource(R.string.classroom_description)) },
                        shape = tfShape,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
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
                                val teacher = selectedTeacher
                                if (teacher != null && name.isNotBlank() && description.isNotBlank()) {
                                    onSubmit(teacher.id, name.trim(), description.trim())
                                }
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
