package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = RabtaGreenSecondary,
    onPrimary = Color.White,
    primaryContainer = RabtaGreenPrimary,
    onPrimaryContainer = Color.White,
    secondary = RabtaGold,
    onSecondary = Color.Black,
    tertiary = RabtaGreenAccent,
    background = RabtaDarkBackground,
    onBackground = RabtaDarkTextPrimary,
    surface = RabtaDarkSurface,
    onSurface = RabtaDarkTextPrimary,
    surfaceVariant = RabtaDarkSurfaceVariant,
    onSurfaceVariant = RabtaDarkTextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = RabtaGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = RabtaLightSurfaceVariant,
    onPrimaryContainer = RabtaGreenPrimary,
    secondary = RabtaGold,
    onSecondary = Color.Black,
    tertiary = RabtaGreenSecondary,
    background = RabtaLightBackground,
    onBackground = RabtaLightTextPrimary,
    surface = RabtaLightSurface,
    onSurface = RabtaLightTextPrimary,
    surfaceVariant = RabtaLightSurfaceVariant,
    onSurfaceVariant = RabtaLightTextSecondary
)

@Composable
fun RabtaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = RabtaGreenPrimary.toArgb()
                window.navigationBarColor = if (darkTheme) RabtaDarkSurface.toArgb() else RabtaLightSurface.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
