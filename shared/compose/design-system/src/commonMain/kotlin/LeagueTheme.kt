package dev.holdbetter.compose.design_system

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier

@Composable
fun LeagueTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDark) DarkAppColors else LightAppColors
    val typography = createTypography()
    val textStyle = createLeagueTextStyle(colors, typography)

    val indicator = ripple(
        color = colors.separator
    )

    CompositionLocalProvider(
        LocalColorsProvider provides colors,
        LocalTextStyleProvider provides textStyle,
        LocalTypographyProvider provides typography,
        LocalContentColor provides colors.textColor,
        LocalIndication provides indicator
    ) {
        Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
            content()
        }
    }
}

object LeagueTheme {
    val colors: LeagueColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColorsProvider.current

    val typography: LeagueTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypographyProvider.current

    val textStyle: LeagueTextStyle
        @Composable
        @ReadOnlyComposable
        get() = LocalTextStyleProvider.current
}