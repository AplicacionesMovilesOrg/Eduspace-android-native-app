package upc.edu.pe.eduspace.features.classrooms.presentation.classroom_detail.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import upc.edu.pe.eduspace.features.classrooms.domain.models.Resource
import upc.edu.pe.eduspace.features.classrooms.domain.models.ResourceType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditResourceDialog(
    resource: Resource,
    onDismiss: () -> Unit,
    onSubmit: (name: String, kindOfResource: String) -> Unit
) {
    var name by remember { mutableStateOf(resource.name) }
    var selectedResourceType by remember {
        mutableStateOf(ResourceType.fromString(resource.kindOfResource) ?: ResourceType.COMPUTER)
    }
    var expanded by remember { mutableStateOf(false) }

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
                            "Edit Resource",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = primaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Resource Name") },
                        singleLine = true,
                        shape = tfShape,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedResourceType.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Resource Type") },
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
                            ResourceType.getAllTypes().forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.displayName) },
                                    onClick = {
                                        selectedResourceType = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

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
                                if (name.isNotBlank()) {
                                    onSubmit(name.trim(), selectedResourceType.displayName)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text("Update")
                        }
                    }
                }
            }
        }
    }
}
