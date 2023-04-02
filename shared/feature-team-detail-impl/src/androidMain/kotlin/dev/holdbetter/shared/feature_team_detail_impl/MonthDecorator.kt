package dev.holdbetter.shared.feature_team_detail_impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
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

internal class MonthDecorator(
    context: Context,
    @ColorInt textColor: Int
) : ItemDecoration() {

    private val circleRadius = 1.5f.px
    private val circleMarginTop = 4.px
    private val circleDiameter = circleRadius * 2
    private val monthsMarginBottom =
        19.px.toInt() + circleDiameter.toInt() + circleMarginTop.toInt()
    private val monthsMarginHorizontal = 10.px

    private val basePaint = Paint().apply {
        color = textColor
    }

    private val monthPaint = TextPaint(basePaint).apply {
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, assetsFont.raleway_regular)
        fontFeatureSettings = "lnum"
        textSize = 11.px
    }

    private val currentMonthPaint = TextPaint(monthPaint).apply {
        typeface = ResourcesCompat.getFont(context, assetsFont.raleway_bold)
    }

    private val months = (1..12).monthsExponent

    private val IntRange.monthsExponent: List<String>
        get() {
            val months = mutableListOf<String>()
            forEach {
                months += if (it > 9) "$it" else "0$it"
            }
            return months
        }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val textWidth = (parent.width - monthsMarginHorizontal * 2) / months.count()

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

        months.forEachIndexed { index, month ->
            val isUserMonth = changerItemDate != null &&
                    changerItemDate.date.monthNumber == month.toInt()

            val middleRectX = (index + .5f) * textWidth * 1f + monthsMarginHorizontal

            c.drawText(
                month,
                middleRectX,
                parent.height - circleDiameter - circleMarginTop,
                chooseTextPaint(isUserMonth)
            )

            if (isUserMonth) {
                c.drawCircle(
                    middleRectX,
                    parent.height * 1f - circleRadius,
                    circleRadius,
                    basePaint
                )
            }
        }
    }

    private fun chooseTextPaint(isUserMonth: Boolean) =
        if (isUserMonth) currentMonthPaint else monthPaint

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = monthsMarginBottom
    }
}