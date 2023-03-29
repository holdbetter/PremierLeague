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

    private val tileShaderPaint = Paint().apply {
        shader = BitmapShader(
            bitmap,
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        )
    }

    private var tileColor: Int? = null

    private val tileAngle =
        ResourcesCompat.getFloat(context.resources, R.dimen.match_card_tile_angle)

    override fun dispatchDraw(canvas: Canvas?) {
        if (tileColor != null) {
            canvas?.save()
            canvas?.rotate(tileAngle)
            canvas?.drawPaint(tileShaderPaint)
            canvas?.restore()
            bitmap.recycle()
        }
        super.dispatchDraw(canvas)
    }

    fun setTileColor(@ColorInt colorInt: Int) {
        this.tileColor = colorInt
        tileShaderPaint.colorFilter = PorterDuffColorFilter(
            colorInt,
            PorterDuff.Mode.SRC_IN
        )
        invalidate()
    }
}