package upc.edu.pe.eduspace.features.shared_spaces.presentation.shared_areas.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedArea
import upc.edu.pe.eduspace.features.shared_spaces.domain.models.SharedSpaceType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSharedAreaDialog(
    sharedArea: SharedArea,
    onDismiss: () -> Unit,
    onConfirm: (type: SharedSpaceType, capacity: Int, description: String) -> Unit
) {
    var selectedType by remember { mutableStateOf(sharedArea.type) }
    var expanded by remember { mutableStateOf(false) }
    var capacity by remember { mutableStateOf(sharedArea.capacity.toString()) }
    var description by remember { mutableStateOf(sharedArea.description) }
    var typeError by remember { mutableStateOf(false) }
    var capacityError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Shared Area") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Dropdown for SharedSpaceType
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedType.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Area Type") },
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        isError = typeError,
                        supportingText = if (typeError) {
                            { Text("Please select an area type") }
                        } else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        SharedSpaceType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    selectedType = type
                                    typeError = false
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = capacity,
                    onValueChange = {
                        capacity = it
                        capacityError = it.toIntOrNull()?.let { num -> num <= 0 } ?: true
                    },
                    label = { Text("Capacity") },
                    isError = capacityError,
                    supportingText = if (capacityError) {
                        { Text("Enter a valid number greater than 0") }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (capacity.toIntOrNull()?.let { it > 0 } == true) {
                        onConfirm(selectedType, capacity.toInt(), description)
                    } else {
                        capacityError = capacity.toIntOrNull()?.let { it <= 0 } ?: true
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
