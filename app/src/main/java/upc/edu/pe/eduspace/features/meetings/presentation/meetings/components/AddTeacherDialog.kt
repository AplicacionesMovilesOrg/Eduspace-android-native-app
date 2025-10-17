package upc.edu.pe.eduspace.features.meetings.presentation.meetings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import upc.edu.pe.eduspace.features.teachers.domain.model.Teacher

@Composable
fun AddTeacherDialog(
    teachers: List<Teacher>,
    onDismiss: () -> Unit,
    onConfirm: (teacherId: Int) -> Unit
) {
    var selectedTeacher by remember { mutableStateOf<Teacher?>(null) }

    val primaryBlue = Color(0xFF2E68B8)

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
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    Modifier
                        .padding(20.dp)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = primaryBlue)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.add_teacher_to_meeting),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = primaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Text(
                        stringResource(R.string.select_teacher),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666)
                    )

                    // Teachers list
                    if (teachers.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(teachers) { teacher ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedTeacher?.id == teacher.id) {
                                            primaryBlue.copy(alpha = 0.1f)
                                        } else {
                                            Color.White
                                        }
                                    ),
                                    onClick = { selectedTeacher = teacher }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Text(
                                            "${teacher.firstName} ${teacher.lastName}",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = Color(0xFF1A1A1A)
                                        )
                                        Text(
                                            teacher.email,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF666666)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(stringResource(R.string.cancel), color = primaryBlue)
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                selectedTeacher?.let { teacher ->
                                    onConfirm(teacher.id)
                                }
                            },
                            enabled = selectedTeacher != null,
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.add))
                        }
                    }
                }
            }
        }
    }
}
