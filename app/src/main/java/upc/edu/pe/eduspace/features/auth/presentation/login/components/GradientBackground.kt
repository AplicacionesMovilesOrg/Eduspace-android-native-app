package upc.edu.pe.eduspace.features.auth.presentation.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import upc.edu.pe.eduspace.core.ui.theme.EduGradientPrimary

@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = EduGradientPrimary)
    ) {
        content()
    }
}