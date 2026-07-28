package dev.holdbetter.compose.design_system

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class LeagueColors(
    val primary: Color,
    val background: Color,
    val textColor: Color,
    val textColorInverse: Color,
    val textColorSecondary: Color,
    val separator: Color,
)