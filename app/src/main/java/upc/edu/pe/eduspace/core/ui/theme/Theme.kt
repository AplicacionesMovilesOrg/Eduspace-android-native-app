package upc.edu.pe.eduspace.core.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = EduBlue500,
    secondary = EduBlue300,
    tertiary = EduInfo
)

private val EduLightColorScheme = lightColorScheme(
    primary = EduBlue700,
    onPrimary = Color.White,
    primaryContainer = EduBlue100,
    onPrimaryContainer = EduBlue900,

    secondary = EduBlue500,
    onSecondary = Color.White,
    secondaryContainer = EduBlue300,
    onSecondaryContainer = EduBlue900,

    tertiary = EduInfo,

    error = EduError,
    onError = Color.White,

    background = EduBackground,
    onBackground = EduTextPrimary,

    surface = EduSurface,
    onSurface = EduTextPrimary,
    onSurfaceVariant = EduTextSecondary,

    outline = EduTextTertiary,
    outlineVariant = EduDivider
)

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> EduLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}