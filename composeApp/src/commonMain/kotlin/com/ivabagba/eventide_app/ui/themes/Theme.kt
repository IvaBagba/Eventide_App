package com.ivabagba.eventide_app.ui.themes

import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7C4DFF),
    secondary = Color(0xFF00E5FF),
    background = Color(0xFF0F111A),
    surface = Color(0xFF181B25),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val NeonColorScheme = darkColorScheme(
    primary = Color(0xFF00E5FF),
    secondary = Color(0xFF7C4DFF),
    background = Color(0xFF050816),
    surface = Color(0xFF101426),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val BlueColorScheme = darkColorScheme(
    primary = Color(0xFF2196F3),
    secondary = Color(0xFF00BCD4),
    background = Color(0xFF07111F),
    surface = Color(0xFF111A2E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1565C0),
    secondary = Color(0xFF00838F),
    background = Color(0xFFF5F7FA),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF101010),
    onSurface = Color(0xFF101010)
)

private val CrimsonDarkColorScheme = darkColorScheme(
    primary = Color(0xFFE53935),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF7F1D1D),
    onPrimaryContainer = Color(0xFFFFDAD6),

    secondary = Color(0xFFFFB4A9),
    onSecondary = Color(0xFF3B0806),
    secondaryContainer = Color(0xFF5C1A16),
    onSecondaryContainer = Color(0xFFFFDAD6),

    tertiary = Color(0xFFFFC1CC),
    onTertiary = Color(0xFF4A1020),
    tertiaryContainer = Color(0xFF6F1D2F),
    onTertiaryContainer = Color(0xFFFFD9E3),

    background = Color(0xFF0F0A0A),
    onBackground = Color(0xFFFFEDEA),

    surface = Color(0xFF181010),
    onSurface = Color(0xFFFFEDEA),

    surfaceVariant = Color(0xFF332222),
    onSurfaceVariant = Color(0xFFE7C0BC),

    surfaceTint = Color(0xFFE53935),

    inverseSurface = Color(0xFFFFEDEA),
    inverseOnSurface = Color(0xFF2C1B1B),
    inversePrimary = Color(0xFFB3261E),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFF9F8C89),
    outlineVariant = Color(0xFF4D3533),

    scrim = Color(0xFF000000)
)

enum class AppThemeType {
    DARK,
    NEON,
    BLUE,
    LIGHT,
    RED
}
@Composable
fun AppTheme(
    theme: AppThemeType = AppThemeType.DARK,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppThemeType.LIGHT -> LightColorScheme
        AppThemeType.DARK -> DarkColorScheme
        AppThemeType.NEON -> NeonColorScheme
        AppThemeType.BLUE -> BlueColorScheme
        AppThemeType.RED -> CrimsonDarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}