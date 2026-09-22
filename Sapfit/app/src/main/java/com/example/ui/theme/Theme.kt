package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val appBackground: Color,
    val cardBackground: Color,
    val insetControlBackground: Color,
    val surfaceContainer: Color,
    val activeSegmentBackground: Color,
    val activeSegmentBorder: Color,
    val borderDefault: Color,
    val borderDropdown: Color,
    val accentPrimary: Color,
    val accentText: Color,
    val accentSoftIcon: Color,
    val onAccent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textMuted: Color,
    val textFaint: Color,
    val progressLocked: Color,
    val progressLearning: Color,
    val progressAhead: Color,
    val dangerRed: Color,
    val navBackground: Color,
    val navBorder: Color
)

val DarkAppColors = AppColors(
    appBackground = Color(0xFF14161C),
    cardBackground = Color(0xFF1E212E),
    insetControlBackground = Color(0xFF1A1D28),
    surfaceContainer = Color(0xFF262A3D),
    activeSegmentBackground = Color(0xFF262C4A),
    activeSegmentBorder = Color(0xFF4A50A8),
    borderDefault = Color(0xFF2A2D3A),
    borderDropdown = Color(0xFF363A52),
    accentPrimary = Color(0xFF6366F1),
    accentText = Color(0xFFA5A9FF),
    accentSoftIcon = Color(0xFF8B8FFA),
    onAccent = Color(0xFFF5F6FF),
    textPrimary = Color(0xFFE2E4EC),
    textSecondary = Color(0xFF7B8296),
    textTertiary = Color(0xFFC5C8D6),
    textMuted = Color(0xFF5A6480),
    textFaint = Color(0xFF4D5266),
    progressLocked = Color(0xFF4ADE80),
    progressLearning = Color(0xFF6366F1),
    progressAhead = Color(0xFF2A2D3A),
    dangerRed = Color(0xFFE88888),
    navBackground = Color(0x8C262A3D),
    navBorder = Color(0x2E8B8FFA)
)

val LightAppColors = AppColors(
    appBackground = Color(0xFFF3F1EC),
    cardBackground = Color(0xFFFFFFFF),
    insetControlBackground = Color(0xFFEAE7DF),
    surfaceContainer = Color(0xFFFFFFFF),
    activeSegmentBackground = Color(0xFFE6E7FA),
    activeSegmentBorder = Color(0xFF6366F1),
    borderDefault = Color(0xFFDDD9CF),
    borderDropdown = Color(0xFFDDD9CF),
    accentPrimary = Color(0xFF5A5EE0),
    accentText = Color(0xFF3F42B8),
    accentSoftIcon = Color(0xFF5457E0),
    onAccent = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF1F2028),
    textSecondary = Color(0xFF5F6373),
    textTertiary = Color(0xFF3D4152),
    textMuted = Color(0xFF8A8E9E),
    textFaint = Color(0xFFA5A9B8),
    progressLocked = Color(0xFF1F9E5A),
    progressLearning = Color(0xFF5A5EE0),
    progressAhead = Color(0xFFE0DCD0),
    dangerRed = Color(0xFFD24A42),
    navBackground = Color(0xE6FFFFFF),
    navBorder = Color(0x336366F1)
)

val LocalAppColors = staticCompositionLocalOf { DarkAppColors }

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}

private val DarkColorScheme = darkColorScheme(
    primary = DarkAppColors.accentPrimary,
    onPrimary = DarkAppColors.onAccent,
    primaryContainer = DarkAppColors.activeSegmentBackground,
    onPrimaryContainer = DarkAppColors.accentText,
    secondary = DarkAppColors.accentSoftIcon,
    onSecondary = DarkAppColors.onAccent,
    background = DarkAppColors.appBackground,
    onBackground = DarkAppColors.textPrimary,
    surface = DarkAppColors.cardBackground,
    onSurface = DarkAppColors.textPrimary,
    surfaceVariant = DarkAppColors.insetControlBackground,
    onSurfaceVariant = DarkAppColors.textSecondary,
    outline = DarkAppColors.borderDefault,
    outlineVariant = DarkAppColors.borderDropdown,
    error = DarkAppColors.dangerRed
)

private val LightColorScheme = lightColorScheme(
    primary = LightAppColors.accentPrimary,
    onPrimary = LightAppColors.onAccent,
    primaryContainer = LightAppColors.activeSegmentBackground,
    onPrimaryContainer = LightAppColors.accentText,
    secondary = LightAppColors.accentSoftIcon,
    onSecondary = LightAppColors.onAccent,
    background = LightAppColors.appBackground,
    onBackground = LightAppColors.textPrimary,
    surface = LightAppColors.cardBackground,
    onSurface = LightAppColors.textPrimary,
    surfaceVariant = LightAppColors.insetControlBackground,
    onSurfaceVariant = LightAppColors.textSecondary,
    outline = LightAppColors.borderDefault,
    outlineVariant = LightAppColors.borderDropdown,
    error = LightAppColors.dangerRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    CompositionLocalProvider(
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
