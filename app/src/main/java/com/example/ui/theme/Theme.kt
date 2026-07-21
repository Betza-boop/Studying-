package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryTealDark,
    secondary = SecondaryBlueDark,
    tertiary = AccentAmber,
    background = DarkBg,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = DarkBg,
    onSecondary = DarkBg,
    onTertiary = DarkBg,
    onBackground = OnBg,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceMuted
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTealLight,
    secondary = SecondaryBlueLight,
    tertiary = AccentAmber,
    background = OnBg,
    surface = OnBg,
    onPrimary = OnBg,
    onSecondary = OnBg,
    onTertiary = OnBg,
    onBackground = DarkBg,
    onSurface = DarkBg
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark mode by default for that eye-safe clinical dashboard look
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
