package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class AppTheme(val displayName: String, val description: String) {
    SYSTEM("System Default", "Follows device appearance"),
    LIGHT("Rose Bloom (Light)", "Classic maternal soft rose"),
    DARK("Night Comfort (Dark)", "Gentle contrast for night reading"),
    CALM("Medical Calm (Teal)", "Soothing clinical teal & sage")
}

data class PreCareExtendedColors(
    val primary: Color,
    val primarySoft: Color,
    val background: Color,
    val pageBackground: Color,
    val cardBackground: Color,
    val border: Color,
    val darkHeading: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val success: Color,
    val successSoft: Color,
    val warning: Color,
    val warningSoft: Color,
    val danger: Color,
    val dangerSoft: Color
)

val LocalPreCareColors = staticCompositionLocalOf {
    PreCareExtendedColors(
        primary = PreCarePrimary,
        primarySoft = PreCarePrimarySoft,
        background = PreCareBackground,
        pageBackground = PreCarePageBackground,
        cardBackground = PreCareCardBackground,
        border = PreCareBorder,
        darkHeading = PreCareDarkHeading,
        primaryText = PreCarePrimaryText,
        secondaryText = PreCareSecondaryText,
        success = PreCareSuccess,
        successSoft = PreCareSuccessSoft,
        warning = PreCareWarning,
        warningSoft = PreCareWarningSoft,
        danger = PreCareDanger,
        dangerSoft = PreCareDangerSoft
    )
}

private val PreCareLightColorScheme = lightColorScheme(
    primary = PreCarePrimary,
    onPrimary = PreCareBackground,
    primaryContainer = PreCarePrimarySoft,
    onPrimaryContainer = PreCarePrimary,
    secondary = PreCareSecondary,
    onSecondary = PreCareBackground,
    secondaryContainer = PreCarePrimarySoft,
    onSecondaryContainer = PreCareSecondary,
    background = PreCareBackground,
    onBackground = PreCarePrimaryText,
    surface = PreCareCardBackground,
    onSurface = PreCarePrimaryText,
    surfaceVariant = PreCarePageBackground,
    onSurfaceVariant = PreCareSecondaryText,
    outline = PreCareBorder,
    error = PreCareDanger,
    onError = PreCareBackground
)

private val PreCareCalmColorScheme = lightColorScheme(
    primary = CalmPrimary,
    onPrimary = PreCareBackground,
    primaryContainer = CalmPrimarySoft,
    onPrimaryContainer = CalmPrimary,
    secondary = CalmSecondary,
    onSecondary = PreCareBackground,
    secondaryContainer = CalmPrimarySoft,
    onSecondaryContainer = CalmSecondary,
    background = PreCareBackground,
    onBackground = PreCarePrimaryText,
    surface = PreCareCardBackground,
    onSurface = PreCarePrimaryText,
    surfaceVariant = PreCarePageBackground,
    onSurfaceVariant = PreCareSecondaryText,
    outline = PreCareBorder,
    error = PreCareDanger,
    onError = PreCareBackground
)

private val PreCareDarkColorScheme = darkColorScheme(
    primary = PreCarePrimary,
    onPrimary = DarkHeading,
    primaryContainer = DarkPrimarySoft,
    onPrimaryContainer = DarkHeading,
    secondary = PreCareSecondary,
    onSecondary = DarkHeading,
    secondaryContainer = DarkPrimarySoft,
    onSecondaryContainer = PreCareSecondary,
    background = DarkBackground,
    onBackground = DarkPrimaryText,
    surface = DarkCardBackground,
    onSurface = DarkPrimaryText,
    surfaceVariant = DarkPageBackground,
    onSurfaceVariant = DarkSecondaryText,
    outline = DarkBorder,
    error = PreCareDanger,
    onError = DarkHeading
)

@Composable
fun PreCareTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (appTheme) {
        AppTheme.SYSTEM -> isSystemDark
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.CALM -> false
    }

    val colorScheme: ColorScheme = when (appTheme) {
        AppTheme.SYSTEM -> if (isSystemDark) PreCareDarkColorScheme else PreCareLightColorScheme
        AppTheme.LIGHT -> PreCareLightColorScheme
        AppTheme.DARK -> PreCareDarkColorScheme
        AppTheme.CALM -> PreCareCalmColorScheme
    }

    val extendedColors = when (appTheme) {
        AppTheme.DARK -> PreCareExtendedColors(
            primary = PreCarePrimary,
            primarySoft = DarkPrimarySoft,
            background = DarkBackground,
            pageBackground = DarkPageBackground,
            cardBackground = DarkCardBackground,
            border = DarkBorder,
            darkHeading = DarkHeading,
            primaryText = DarkPrimaryText,
            secondaryText = DarkSecondaryText,
            success = PreCareSuccess,
            successSoft = Color(0xFF132B1B),
            warning = PreCareWarning,
            warningSoft = Color(0xFF332712),
            danger = PreCareDanger,
            dangerSoft = Color(0xFF3A1818)
        )
        AppTheme.CALM -> PreCareExtendedColors(
            primary = CalmPrimary,
            primarySoft = CalmPrimarySoft,
            background = PreCareBackground,
            pageBackground = PreCarePageBackground,
            cardBackground = PreCareCardBackground,
            border = CalmPrimaryBorder,
            darkHeading = PreCareDarkHeading,
            primaryText = PreCarePrimaryText,
            secondaryText = PreCareSecondaryText,
            success = PreCareSuccess,
            successSoft = PreCareSuccessSoft,
            warning = PreCareWarning,
            warningSoft = PreCareWarningSoft,
            danger = PreCareDanger,
            dangerSoft = PreCareDangerSoft
        )
        else -> PreCareExtendedColors(
            primary = PreCarePrimary,
            primarySoft = PreCarePrimarySoft,
            background = PreCareBackground,
            pageBackground = PreCarePageBackground,
            cardBackground = PreCareCardBackground,
            border = PreCareBorder,
            darkHeading = PreCareDarkHeading,
            primaryText = PreCarePrimaryText,
            secondaryText = PreCareSecondaryText,
            success = PreCareSuccess,
            successSoft = PreCareSuccessSoft,
            warning = PreCareWarning,
            warningSoft = PreCareWarningSoft,
            danger = PreCareDanger,
            dangerSoft = PreCareDangerSoft
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val statusBarColor = colorScheme.background.toArgb()
                val navBarColor = colorScheme.background.toArgb()
                window.statusBarColor = statusBarColor
                window.navigationBarColor = navBarColor
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(LocalPreCareColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PreCareTypography,
            content = content
        )
    }
}
