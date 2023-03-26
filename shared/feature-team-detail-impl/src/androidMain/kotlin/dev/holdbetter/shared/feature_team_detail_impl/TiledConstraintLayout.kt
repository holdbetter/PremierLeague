package dev.holdbetter.shared.feature_team_detail_impl

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import dev.holdbetter.assets.assetsDrawable
import dev.holdbetter.assets.px
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class TiledConstraintLayout @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attr, defStyleAttr) {

    private val bitmapScale = .4f
    private val bitmap =
        requireNotNull(AppCompatResources.getDrawable(context, assetsDrawable.league_logo_mini))
            .run {
                toBitmap(
                    (intrinsicWidth * bitmapScale).toInt(),
                    (intrinsicHeight * bitmapScale).toInt()
                )
            }

    private val tileStartX = -bitmap.width / 2f
    private val tileStartY = 0f
    private val tilePadding = 1.px

    private val matrix = Matrix()

    private var tileColor: Int? = null

    private val tileAngle = ResourcesCompat.getFloat(context.resources, R.dimen.match_card_tile_angle)

    private val tilePaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (tileColor != null) {
            val radian = Math.toRadians(abs(tileAngle.toDouble()))

            /* triangle

                (degree)
                   |\
                 a | \ c
                   |__\
                    b

             */
            val c = bottom / cos(radian)
            val b: Float = (c * sin(radian)).toFloat()
            var tiles = 0

            canvas?.save()
            canvas?.rotate(tileAngle)

            val tileBuffer = 2
            val tileXCount = (right + c * tileBuffer / (bitmap.width + tilePadding)).toInt()
            val tileYCount = tileBuffer + bottom / (bitmap.height + tilePadding).toInt()

            for (y in 0..tileYCount) {
                val dY = (y * (bitmap.height + tilePadding) + tileStartY)
                for (x in 0..tileXCount) {
                    matrix.setTranslate(tiles * (bitmap.width + tilePadding) + tileStartX - b, dY)
                    canvas?.drawBitmap(bitmap, matrix, tilePaint)
                    tiles += 1
                }
                tiles = 0
            }

            canvas?.restore()
        }
        super.dispatchDraw(canvas)
    }

    fun setTileColor(@ColorInt colorInt: Int) {
        this.tileColor = colorInt
        tilePaint.colorFilter = PorterDuffColorFilter(
            colorInt,
            PorterDuff.Mode.SRC_IN
        )
        invalidate()
    }
}