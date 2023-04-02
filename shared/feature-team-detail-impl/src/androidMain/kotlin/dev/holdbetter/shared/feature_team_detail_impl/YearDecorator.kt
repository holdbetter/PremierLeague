package dev.holdbetter.shared.feature_team_detail_impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import dev.holdbetter.assets.assetsFont
import dev.holdbetter.assets.px

internal class YearDecorator(context: Context, @ColorInt textColor: Int) : ItemDecoration() {

    private val dp16 = 16.px

    private val marginRight = dp16
    private val negativeDecoratorMarginTop = 11.px
    private val decoratorMarginBottom = 5.px

    private val rectHeight = dp16
    private val rectWidth = 38.px

    private val backgroundRadius = dp16

    private val fontFeatureMargin = 1

    private val backgroundPaint = Paint()

    private val textPaint = TextPaint().apply {
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, assetsFont.raleway_medium)
        textSize = 10.px
        color = textColor
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val firstVisiblePos =
            (parent.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val lastVisiblePos =
            (parent.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        val midVisiblePos = (firstVisiblePos + lastVisiblePos) / 2

        val changerItemDate = if (firstVisiblePos != NO_POSITION) {
            (parent.adapter as? DateAdapter)?.currentList?.let {
                val realMid = midVisiblePos % it.count()
                it[realMid]
            }
        } else {
            null
        }

        val rectF = RectF(
            parent.right - rectWidth - marginRight,
            0f - negativeDecoratorMarginTop,
            parent.right.toFloat() - marginRight,
            0f + rectHeight - negativeDecoratorMarginTop
        )

        c.drawRoundRect(
            rectF,
            backgroundRadius,
            backgroundRadius,
            backgroundPaint
        )

        changerItemDate?.let {
            c.drawText(
                changerItemDate.date.year.toString(),
                rectF.centerX(),
                rectF.centerY() + textPaint.fontMetrics.bottom + fontFeatureMargin,
                textPaint
            )
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = (rectHeight - negativeDecoratorMarginTop + decoratorMarginBottom).toInt()
    }

    fun setBackgroundColor(@ColorInt teamColor: Int) {
        backgroundPaint.color = teamColor
    }
}