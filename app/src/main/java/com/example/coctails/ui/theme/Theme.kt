package com.example.coctails.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldAccent,
    onPrimary = OnDarkPrimary,
    primaryContainer = TealAccent,
    onPrimaryContainer = SoftWhite,
    secondary = TealAccent,
    onSecondary = OnDarkPrimary,
    background = MidnightBlue,
    onBackground = SoftWhite,
    surface = DeepSlate,
    onSurface = SoftWhite,
    surfaceVariant = SlateVariant,
    onSurfaceVariant = SecondaryText,
    outline = SecondaryText
)

@Composable
fun CoctailsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
