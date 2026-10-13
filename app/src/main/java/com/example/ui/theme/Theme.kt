package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = MarigoldOrange,
    onPrimary = OnLightPrimary,
    primaryContainer = LightSurfaceVariant,
    onPrimaryContainer = RoseDarkRed,
    secondary = RoseRed,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFCE4EC),
    onSecondaryContainer = RoseDarkRed,
    tertiary = LeafGreen,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = Color(0xFF261810),
    surface = LightSurface,
    onSurface = Color(0xFF261810),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF4E342E)
)

private val DarkColorScheme = darkColorScheme(
    primary = MarigoldAmber,
    onPrimary = Color.Black,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = GoldYellow,
    secondary = Color(0xFFF48FB1),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF52122B),
    onSecondaryContainer = Color(0xFFFFD8E4),
    tertiary = Color(0xFF81C784),
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = Color(0xFFFCE4EC),
    surface = DarkSurface,
    onSurface = Color(0xFFFCE4EC),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFFFCC80)
)

@Composable
fun RavindraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to prioritize custom signature floral theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
