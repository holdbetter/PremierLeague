package dev.holdbetter.compose.design_system

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalColorsProvider = compositionLocalOf<LeagueColors> {
    error("Colors object is not provided")
}

internal val LocalTypographyProvider = staticCompositionLocalOf<LeagueTypography> {
    error("Typography object is not provided")
}

internal val LocalTextStyleProvider = compositionLocalOf<LeagueTextStyle> {
    error("TextStyle object is not provided")
}