package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = TandisPrimary,
    onPrimary = TandisSurface,
    primaryContainer = TandisPrimaryLight,
    onPrimaryContainer = TandisPrimaryDark,
    secondary = TandisSecondary,
    onSecondary = TandisTextPrimary,
    secondaryContainer = TandisSecondaryHover,
    background = TandisBackground,
    onBackground = TandisTextPrimary,
    surface = TandisSurface,
    onSurface = TandisTextPrimary,
    surfaceVariant = TandisSurfaceVariant,
    onSurfaceVariant = TandisTextSecondary,
    outline = TandisBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = TandisPrimary,
    onPrimary = TandisSurface,
    primaryContainer = TandisPrimaryDark,
    onPrimaryContainer = TandisPrimaryLight,
    secondary = TandisSecondary,
    onSecondary = TandisTextPrimary,
    background = TandisPrimaryDeep,
    onBackground = TandisSurface,
    surface = TandisPrimaryDark,
    onSurface = TandisSurface,
    surfaceVariant = TandisPrimaryDeep,
    onSurfaceVariant = TandisBorderLight,
    outline = TandisBorder
)

@Composable
fun TandisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TandisTypography,
            content = content
        )
    }
}
