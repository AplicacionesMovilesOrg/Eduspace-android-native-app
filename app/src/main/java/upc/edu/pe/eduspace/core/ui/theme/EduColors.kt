package upc.edu.pe.eduspace.core.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Eduspace Professional Color Palette
 * Professional educational institutional blue theme
 */

// Primary Blue Tones (Core Brand)
val EduBlue900 = Color(0xFF1A4D7A)      // Deep blue (darkest)
val EduBlue700 = Color(0xFF2E68B8)      // Primary blue
val EduBlue500 = Color(0xFF4A90E2)      // Light blue
val EduBlue300 = Color(0xFF7CB3F5)      // Very light blue
val EduBlue100 = Color(0xFFB8D9FF)      // Pale blue tint

// Semantic Colors
val EduSuccess = Color(0xFF4CAF50)      // Green (success states)
val EduWarning = Color(0xFFFF9800)      // Orange (warning states)
val EduError = Color(0xFFD32F2F)        // Red (error/delete - darker than FF5252)
val EduInfo = Color(0xFF2196F3)         // Info blue

// Neutral Colors
val EduTextPrimary = Color(0xFF1A1A1A)  // Dark gray text
val EduTextSecondary = Color(0xFF666666) // Medium gray text
val EduTextTertiary = Color(0xFF9E9E9E) // Light gray text
val EduBackground = Color(0xFFF5F5F5)   // Light gray background
val EduSurface = Color(0xFFFFFFFF)      // White surface
val EduDivider = Color(0xFFE0E0E0)      // Light gray divider

// Shadow (for proper depth with low alpha)
val EduShadow = Color(0xFF000000)       // Pure black for shadows

/**
 * Gradient Definitions
 * Professional 2-tone blue gradients
 */

// Primary gradient (header, cards, prominent elements)
val EduGradientPrimary = Brush.linearGradient(
    colors = listOf(EduBlue700, EduBlue500)
)

// Background gradient (subtle for screens)
val EduGradientBackground = Brush.verticalGradient(
    colors = listOf(EduBlue100, Color(0xFFF5F5F5))
)

// Header gradient (drawer, welcome cards)
val EduGradientHeader = Brush.verticalGradient(
    colors = listOf(EduBlue700, EduBlue500)
)
