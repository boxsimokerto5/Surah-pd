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

private val DarkColorScheme = darkColorScheme(
    primary = SoftBlueGradientEnd,
    onPrimary = Color(0xFF041E2B),
    primaryContainer = SoftBluePrimaryDark,
    onPrimaryContainer = SoftBlueLight,
    secondary = SoftWarmGold,
    onSecondary = Color(0xFF261900),
    secondaryContainer = SoftWarmGold.copy(alpha = 0.2f),
    onSecondaryContainer = SoftWarmGoldLight,
    tertiary = SoftBlueGradientStart,
    onTertiary = Color.White,
    background = DarkOceanBg,
    onBackground = Color(0xFFE4F0F6),
    surface = DarkOceanSurface,
    onSurface = Color(0xFFE4F0F6),
    surfaceVariant = DarkOceanSurfaceVariant,
    onSurfaceVariant = Color(0xFFBED5E2),
    outline = DarkOceanBorder
)

private val LightColorScheme = lightColorScheme(
    primary = SoftBluePrimary,
    onPrimary = Color.White,
    primaryContainer = SoftBlueContainer,
    onPrimaryContainer = SoftBlueOnContainer,
    secondary = SoftWarmGold,
    onSecondary = Color.White,
    secondaryContainer = SoftWarmGoldLight,
    onSecondaryContainer = Color(0xFF4A3408),
    tertiary = SoftBlueGradientStart,
    onTertiary = Color.White,
    background = SoftIceBackground,
    onBackground = ArabicMidnightText,
    surface = SoftPureSurface,
    onSurface = ArabicMidnightText,
    surfaceVariant = SoftSurfaceVariant,
    onSurfaceVariant = TranslationCharcoalText,
    outline = SoftCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
