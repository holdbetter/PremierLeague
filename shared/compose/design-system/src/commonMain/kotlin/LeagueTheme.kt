package dev.holdbetter.compose.design_system

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun LeagueTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDark) DarkAppColors else LightAppColors
    val typography = createTypography()
    val textStyle = createLeagueTextStyle(colors, typography)

    CompositionLocalProvider(
        LocalColorsProvider provides colors,
        LocalTextStyleProvider provides textStyle,
        LocalTypographyProvider provides typography,
        LocalContentColor provides colors.textColor
    ) {
        content()
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