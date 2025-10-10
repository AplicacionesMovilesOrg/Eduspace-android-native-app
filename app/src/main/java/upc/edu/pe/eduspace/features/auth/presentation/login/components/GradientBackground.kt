package upc.edu.pe.eduspace.features.auth.presentation.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E88E5),
                        Color(0xFF64B5F6),
                        Color(0xFF81C784),
                        Color(0xFFFFEB3B)
                    )
                )
            )
    ) {
        content()
    }
}