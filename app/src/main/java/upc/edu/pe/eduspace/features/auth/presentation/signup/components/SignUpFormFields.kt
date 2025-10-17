package upc.edu.pe.eduspace.features.auth.presentation.signup.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import upc.edu.pe.eduspace.R

@Composable
fun SignUpFormFields(
    firstName: String,
    lastName: String,
    email: String,
    dni: String,
    address: String,
    phone: String,
    username: String,
    password: String,
    firstNameError: String?,
    lastNameError: String?,
    emailError: String?,
    dniError: String?,
    phoneError: String?,
    usernameError: String?,
    passwordError: String?,
    isPasswordVisible: MutableState<Boolean>,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onDniChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF1976D2),
        unfocusedBorderColor = Color(0xFFE0E0E0),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color(0xFFFAFAFA),
        errorBorderColor = MaterialTheme.colorScheme.error
    )

    OutlinedTextField(
        value = firstName,
        onValueChange = onFirstNameChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.teacher_first_name)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        isError = firstNameError != null,
        supportingText = firstNameError?.let {
            { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
        },
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors,
        singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = lastName,
        onValueChange = onLastNameChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.teacher_last_name)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        isError = lastNameError != null,
        supportingText = lastNameError?.let {
            { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
        },
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors,
        singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = phone,
        onValueChange = onPhoneChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.teacher_phone)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        isError = phoneError != null,
        supportingText = phoneError?.let {
            { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
        },
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors,
        singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.email)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = emailError != null,
        supportingText = emailError?.let {
            { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
        },
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors,
        singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.username)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        isError = usernameError != null,
        supportingText = usernameError?.let {
            { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
        },
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors,
        singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.password)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                Icon(
                    imageVector = if (isPasswordVisible.value)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color(0xFF757575)
                )
            }
        },
        visualTransformation = if (isPasswordVisible.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = passwordError != null,
        supportingText = passwordError?.let {
            { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
        },
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors,
        singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = dni,
        onValueChange = onDniChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.teacher_dni)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Badge,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = dniError != null,
        supportingText = dniError?.let {
            { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
        },
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors,
        singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = address,
        onValueChange = onAddressChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.teacher_address)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors,
        singleLine = true
    )
}