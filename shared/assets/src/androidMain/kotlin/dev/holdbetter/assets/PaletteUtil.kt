package dev.holdbetter.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.bumptech.glide.request.FutureTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

suspend fun createPalette(futureBitmap: FutureTarget<Bitmap>): Palette =
    withContext(Dispatchers.IO) {
        Palette.from(futureBitmap.get()).generate()
    }

fun Palette.generateTeamColor(context: Context): Int {

    fun Palette.dominant(context: Context) =
        getDominantColor(context.getColor(R.color.leagueColorPrimary))

    val hsl = floatArrayOf(0f, 0f, 0f)
    ColorUtils.colorToHSL(dominant(context), hsl)
    val (hue, sat, lum) = hsl
    hsl[1] = sat * .9f
    return ColorUtils.HSLToColor(hsl)
}

@ColorInt
fun tileColor(@ColorInt dominant: Int, isDarkMode: Boolean): Int {
    val hsl = floatArrayOf(0f, 0f, 0f)
    return if (!isDarkMode) {
        ColorUtils.colorToHSL(dominant, hsl)
        val (hue, sat, lum) = hsl
        hsl[1] = sat * .9f
        val dimmedColor = ColorUtils.HSLToColor(hsl)
        ColorUtils.setAlphaComponent(dimmedColor, (255 * 0.1).roundToInt())
    } else {
        ColorUtils.setAlphaComponent(Color.BLACK, (255 * 0.5).roundToInt())
    }
}

@ColorInt
fun cardStartColor(
    @ColorInt teamColor: Int,
    @ColorInt defaultColor: Int,
    isDarkMode: Boolean
): Int {
    return if (isDarkMode) {
        val hsl = floatArrayOf(0f, 0f, 0f)
        ColorUtils.colorToHSL(teamColor, hsl)
        val (hue, sat, lum) = hsl
        hsl[0] = (hue + 20) % 360
        hsl[1] = .2f
        hsl[2] = .3f

        ColorUtils.HSLToColor(hsl)
    } else {
        defaultColor
    }
}

@ColorInt
fun cardEndColor(
    @ColorInt teamColor: Int,
    @ColorInt defaultColor: Int,
    isDarkMode: Boolean
): Int {
    return if (isDarkMode) {
        val hsl = floatArrayOf(0f, .0f, .05f)
        ColorUtils.HSLToColor(hsl)
    } else {
        defaultColor
    }
}

fun Context.getActionDrawable(
    isDarkMode: Boolean,
    @ColorInt color: Int,
    isMini: Boolean
): Drawable {
    val actionDrawable = ResourcesCompat.getDrawable(
        resources,
        R.drawable.shape_team_detail_action,
        theme
    ) as GradientDrawable

    actionDrawable.apply {
        mutate()
        val strokeColor = if (isDarkMode) {
            Color.TRANSPARENT
        } else {
            if (isMini) {
                getColor(R.color.leagueTextColor)
            } else {
                color
            }
        }
        val strokeWidth = if (isMini) {
            resources.getDimension(R.dimen.action_mini_stroke_width)
        } else {
            resources.getDimension(R.dimen.action_stroke_width)
        }
        val backgroundColor = if (isDarkMode) color else Color.TRANSPARENT
        actionDrawable.setStroke(strokeWidth.toInt(), strokeColor)
        actionDrawable.setColor(backgroundColor)
    }

    return actionDrawable
}

@ColorInt
fun Context.textColor(@ColorInt teamColor: Int, isDarkMode: Boolean): Int {
    return if (isDarkMode) {
        getColor(R.color.leagueTextColor)
    } else {
        teamColor
    }
}