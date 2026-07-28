package dev.holdbetter.compose.design_system

object Design {
    object Color {
        val Primary = Raw.Colors.Purple

        val Background = Raw.Colors.White50

        val TextColor = Raw.Colors.Black
        val TextColorInverse = Raw.Colors.White
        val TextColorSecondary = Raw.Colors.Black500

        val Separator = Raw.Colors.Purple50
    }
}

val LightAppColors = LeagueColors(
    primary = Raw.Colors.Purple,
    background = Raw.Colors.White50,
    textColor = Raw.Colors.Black,
    textColorInverse = Raw.Colors.White,
    textColorSecondary = Raw.Colors.Black500,
    separator = Raw.Colors.Purple50
)

val DarkAppColors = LeagueColors(
    primary = Raw.Colors.Purple,
    background = Raw.Colors.Black850,
    textColor = Raw.Colors.White,
    textColorInverse = Raw.Colors.Black,
    textColorSecondary = Raw.Colors.White,
    separator = Raw.Colors.Purple200
)