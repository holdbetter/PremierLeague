package dev.holdbetter.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
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
    val defaultColor = context.getColor(R.color.leagueColorPrimary)

    fun Palette.dominant() = with(getDominantColor(defaultColor)) {
        val hsl = floatArrayOf(0f, 0f, 0f)
        ColorUtils.colorToHSL(this, hsl)
        val (hue, sat, lum) = hsl
        if (lum > .9f || lum < .3f) {
            defaultColor
        } else {
            this
        }
    }

    val hsl = floatArrayOf(0f, 0f, 0f)
    ColorUtils.colorToHSL(dominant(), hsl)
    val (hue, sat, lum) = hsl
    hsl[1] = sat * .85f
    return ColorUtils.HSLToColor(hsl)
}

@ColorInt
fun tileColor(@ColorInt dominant: Int, isDarkMode: Boolean): Int {
    val hsl = floatArrayOf(0f, 0f, 0f)
    return if (!isDarkMode) {
        ColorUtils.colorToHSL(dominant, hsl)
        val (hue, sat, lum) = hsl
        hsl[1] = sat * .2f
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
        hsl[0] = (hue + 0) % 360
        hsl[1] = sat * .5f
        hsl[2] = .35f

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
    @ColorInt color: Int
): Drawable {
    val actionDrawable = AppCompatResources.getDrawable(
        this,
        R.drawable.shape_team_detail_action,
    ) as GradientDrawable

    actionDrawable.apply {
        mutate()
        setColor(color)
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