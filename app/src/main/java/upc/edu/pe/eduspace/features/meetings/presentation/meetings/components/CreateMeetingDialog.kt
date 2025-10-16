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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeetingDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, date: String, start: String, end: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    fun Long.toFormattedDateString() : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date(this))
    }

    var selectedDateText by remember {mutableStateOf("")}

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    var showDatePicker by remember { mutableStateOf(false) }



    // Time pickers state
    val startTimeState = rememberTimePickerState(initialHour = 9, initialMinute = 0, is24Hour = true)
    val endTimeState = rememberTimePickerState(initialHour = 11, initialMinute = 0, is24Hour = true)
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val startTime by remember {
        derivedStateOf {
            String.format(Locale.getDefault(), "%02d:%02d:00", startTimeState.hour, startTimeState.minute)
        }
    }

    val endTime by remember {
        derivedStateOf {
            String.format(Locale.getDefault(), "%02d:%02d:00", endTimeState.hour, endTimeState.minute)
        }
    }

    val primaryBlue = Color(0xFF2E68B8)
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
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    Modifier
                        .padding(20.dp)
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Event, contentDescription = null, tint = primaryBlue)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Create Meeting",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = primaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        singleLine = true,
                        shape = tfShape,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        minLines = 2,
                        maxLines = 3,
                        shape = tfShape,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Date picker field
                    OutlinedTextField(
                        value = selectedDateText,
                        onValueChange = { },
                        label = { Text("Date") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, contentDescription = "Select date")
                            }
                        },
                        shape = tfShape,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Time pickers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = { },
                            label = { Text("Start Time") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showStartTimePicker = true }) {
                                    Icon(Icons.Default.Schedule, contentDescription = "Select start time")
                                }
                            },
                            shape = tfShape,
                            colors = tfColors,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = endTime,
                            onValueChange = { },
                            label = { Text("End Time") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showEndTimePicker = true }) {
                                    Icon(Icons.Default.Schedule, contentDescription = "Select end time")
                                }
                            },
                            shape = tfShape,
                            colors = tfColors,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", color = primaryBlue)
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (title.isNotBlank() && description.isNotBlank() &&
                                    selectedDateText.isNotBlank()
                                ) {
                                    onConfirm(
                                        title.trim(),
                                        description.trim(),
                                        selectedDateText,
                                        startTime,
                                        endTime
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Create")
                        }
                    }
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDateText = it.toFormattedDateString()
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Start Time Picker Dialog
    if (showStartTimePicker) {
        TimePickerDialog(
            onDismiss = { showStartTimePicker = false },
            onConfirm = { showStartTimePicker = false }
        ) {
            TimePicker(state = startTimeState)
        }
    }

    // End Time Picker Dialog
    if (showEndTimePicker) {
        TimePickerDialog(
            onDismiss = { showEndTimePicker = false },
            onConfirm = { showEndTimePicker = false }
        ) {
            TimePicker(state = endTimeState)
        }
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
