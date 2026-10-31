package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = TatTelDarkGreen,
    onPrimary = TatTelLightBg,
    primaryContainer = TatTelLightGreen,
    onPrimaryContainer = TatTelDarkGreen,
    secondary = TatTelAccent,
    onSecondary = TatTelLightBg,
    secondaryContainer = TatTelLightGreen,
    onSecondaryContainer = TatTelMainText,
    background = TatTelLightBg,
    onBackground = TatTelMainText,
    surface = TatTelSurface,
    onSurface = TatTelMainText,
    surfaceVariant = TatTelLightGreen,
    onSurfaceVariant = TatTelMainText,
    outline = TatTelCardBorder,
    error = TatTelError,
    errorContainer = TatTelErrorBg,
    onError = TatTelLightBg,
    onErrorContainer = TatTelError
)

private val DarkColorScheme = darkColorScheme(
    primary = TatTelLightGreen,
    onPrimary = TatTelDarkGreen,
    primaryContainer = TatTelDarkGreen,
    onPrimaryContainer = TatTelLightGreen,
    secondary = TatTelLightGreen,
    onSecondary = TatTelDarkGreen,
    secondaryContainer = TatTelDarkGreen,
    onSecondaryContainer = TatTelLightGreen,
    background = TatTelMainText,
    onBackground = TatTelLightBg,
    surface = Color(0xFF2A3129),
    onSurface = TatTelLightBg,
    surfaceVariant = Color(0xFF333B32),
    onSurfaceVariant = TatTelLightGreen,
    outline = Color(0xFF4A5548),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun TatTelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
