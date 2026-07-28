package dev.holdbetter.compose.design_system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font

data class LeagueTextStyle(
    val headline1: TextStyle,
    val headline1SameNumbers: TextStyle,
    val headline2: TextStyle,
    val headline2SameNumbers: TextStyle,
    val headline3: TextStyle,
    val headline3SameNumbers: TextStyle,
    val content: TextStyle,
    val contentSameNumbers: TextStyle,
)

@Immutable
data class LeagueTypography(
    val regular: FontFamily,
    val medium: FontFamily,
    val semibold: FontFamily,
    val bold: FontFamily,
    val extraBold: FontFamily,
    val boldItalic: FontFamily,
)

@Composable
fun createTypography(): LeagueTypography {
    return LeagueTypography(
        regular = createRalewayVariant(FontWeight.Normal),
        medium = createRalewayVariant(FontWeight.Medium),
        semibold = createRalewayVariant(FontWeight.SemiBold),
        bold = createRalewayVariant(FontWeight.Bold),
        extraBold = createRalewayVariant(FontWeight.ExtraBold),
        boldItalic = createRalewayBoldItalic(),
    )
}

@Composable
fun createLeagueTextStyle(
    colors: LeagueColors,
    leagueTypography: LeagueTypography
): LeagueTextStyle {
    val baseColor = colors.textColor

    val headline1 = TextStyle(
        color = baseColor,
        fontSize = 23.sp,
        fontFamily = leagueTypography.bold
    )

    val headline2 = TextStyle(
        color = baseColor,
        fontSize = 16.sp,
        fontFamily = leagueTypography.semibold
    )

    val headline3 = TextStyle(
        color = baseColor,
        fontSize = 14.sp,
        fontFamily = leagueTypography.medium
    )

    val content = TextStyle(
        color = baseColor,
        fontSize = 11.sp,
        fontFamily = leagueTypography.medium
    )

    fun TextStyle.makeLnum(): TextStyle = copy(fontFeatureSettings = "lnum")

    return LeagueTextStyle(
        headline1 = headline1,
        headline2 = headline2,
        headline3 = headline3,
        content = content,
        headline1SameNumbers = headline1.makeLnum(),
        headline2SameNumbers = headline2.makeLnum(),
        headline3SameNumbers = headline3.makeLnum(),
        contentSameNumbers = content.makeLnum(),
    )
}

@Composable
private fun createRalewayVariant(weight: FontWeight): FontFamily {
    return FontFamily(
        Font(
            resource = Res.font.raleway_variable,
            weight = weight
        )
    )
}

@Composable
private fun createRalewayBoldItalic(): FontFamily {
    return FontFamily(
        Font(
            resource = Res.font.raleway_italic_bold
        )
    )
}