package dev.holdbetter.shared.feature_team_detail_impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.SizeF
import android.view.View
import androidx.annotation.Px
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.holdbetter.assets.assetsColor
import dev.holdbetter.assets.assetsDimen
import dev.holdbetter.assets.assetsFont
import dev.holdbetter.assets.px
import dev.holdbetter.shared.feature_team_detail.MonthResult
import kotlinx.datetime.Month
import kotlin.math.ceil

internal class GroupGridDecorator(
    private val context: Context,
    private val spanCount: Int,
    @Px private val verticalOffsetBtwRows: Float,
    parentWidth: Int,
    @Px itemWidth: Float,
    @Px recyclerMarginHorizontal: Float = 0f
) : RecyclerView.ItemDecoration() {

    companion object {
        const val HEADER_MARGIN_HORIZONTAL = 6

        const val HEADER_TEXT_MARGIN_BOTTOM = 18
        const val HEADER_RECT_MARGIN_BOTTOM = 15

        const val HEADER_BTW_VALUES_MARGIN = 7
    }

    private val recyclerMarginSide = recyclerMarginHorizontal / 2f
    private val widthAvailable = parentWidth - recyclerMarginHorizontal
    private val overallItemsWidth = itemWidth * spanCount
    private val spaceBetween = widthAvailable - overallItemsWidth
    private val spacing = spaceBetween.toInt() / (spanCount - 1)

    private val groupHeaderTextSize = resources.getDimension(R.dimen.result_month_header_text_size)
    private val groupValueTextSize =
        resources.getDimension(R.dimen.result_month_header_value_text_size)

    private val groupValueWidth = resources.getDimension(R.dimen.result_month_header_value_width)
    private val groupValueHeight = resources.getDimension(R.dimen.result_month_header_value_height)

    private val headerMarginHorizontal = HEADER_MARGIN_HORIZONTAL.px
    private val headerTextMarginBottom = HEADER_TEXT_MARGIN_BOTTOM.px
    private val headerRectMarginBottom = HEADER_RECT_MARGIN_BOTTOM.px
    private val headerBtwResultsMargin = HEADER_BTW_VALUES_MARGIN.px

    private val winColor = context.getColor(R.color.result_month_header_win)
    private val loseColor = context.getColor(R.color.result_month_header_lose)
    private val drawColor = context.getColor(R.color.result_month_header_draw)

    private val winTextColor = context.getColor(R.color.result_month_header_text_win)
    private val loseTextColor = context.getColor(R.color.result_month_header_text_lose)
    private val drawTextColor = context.getColor(R.color.result_month_header_text_draw)

    private val strokeDimen = resources.getDimension(assetsDimen.action_mini_stroke_width)

    private val rectSize = SizeF(groupValueWidth - strokeDimen, groupValueHeight - strokeDimen)
    private val rectRadius = resources.getDimension(R.dimen.result_month_header_radius)

    private val rectPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.values()[resources.getInteger(R.integer.result_header_style)]
        strokeWidth = strokeDimen
    }

    private val headerTextPaint = TextPaint().apply {
        isAntiAlias = true
        typeface = ResourcesCompat.getFont(context, assetsFont.raleway_medium)
        color = context.getColor(assetsColor.leagueTextColor)
        textSize = groupHeaderTextSize
    }

    private val valueTextPaint = TextPaint(headerTextPaint).apply {
        textAlign = Paint.Align.CENTER
        textSize = groupValueTextSize
    }

    private val resources
        get() = context.resources

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val grid = (parent.layoutManager as GridLayoutManager)
        val spanLookup = grid.spanSizeLookup as GroupSpanLookup
        val position = parent.getChildAdapterPosition(view)
        val spanIndex = spanLookup.getSpanIndex(position, spanCount)

        val headerTextMargin = ceil(headerTextPaint.textSize + headerTextMarginBottom).toInt()
        if (position < spanCount) {
            val spanGroupIndex = spanLookup.getSpanGroupIndex(position, spanCount)
            if (spanGroupIndex == 0) {
                outRect.top = headerTextMargin
            }
        }

        var bottom = verticalOffsetBtwRows
        if (spanLookup.isItemInLastRowOfGroup(position)) {
            bottom += headerTextMargin
        }

        outRect.bottom = ceil(bottom).toInt()

        outRect.left = spanIndex * spacing / spanCount
        outRect.right = spacing - (spanIndex + 1) * spacing / spanCount
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val adapter = parent.adapter as MatchesAdapter
        for (i in 0 until parent.childCount) {
            val view = parent[i]
            val position = parent.getChildAdapterPosition(view)
            if (position != RecyclerView.NO_POSITION) {
                val top = view.top
                val grid = (parent.layoutManager as GridLayoutManager)
                val spanLookup = grid.spanSizeLookup as GroupSpanLookup
                if (spanLookup.isFirstItemInGroup(position)) {
                    val item = requireNotNull(adapter.currentList[position].startDate)

                    c.drawHeaderText(
                        month = item.month,
                        gridLeft = parent.left,
                        gridItemTop = top
                    )

                    val monthResult = adapter.getMonthResult(item.month)

                    c.drawResult(monthResult, Result.LOSE, parent.right, top)
                    c.drawResult(monthResult, Result.WIN, parent.right, top)
                    c.drawResult(monthResult, Result.DRAW, parent.right, top)
                }
            }
        }
    }

    private fun Canvas.drawHeaderText(month: Month, gridLeft: Int, gridItemTop: Int) {
        drawText(
            month.toString().lowercase(),
            gridLeft + headerMarginHorizontal - recyclerMarginSide,
            gridItemTop.toFloat() - headerTextMarginBottom,
            headerTextPaint
        )
    }

    private fun Canvas.drawResult(
        monthResult: MonthResult,
        result: Result,
        gridRight: Int,
        gridItemTop: Int
    ) {
        val rectPaint = getResultPaint(result)
        val itemWidth = rectSize.width

        val rectTop = gridItemTop - headerRectMarginBottom - rectSize.height
        val rectBottom = gridItemTop - headerRectMarginBottom
        val right = gridRight - recyclerMarginSide - headerMarginHorizontal
        val rect: RectF = when (result) {
            Result.WIN -> RectF(
                right - itemWidth,
                rectTop,
                right,
                rectBottom
            )
            Result.LOSE -> RectF(
                right - itemWidth * 2 - headerBtwResultsMargin,
                rectTop,
                right - itemWidth - headerBtwResultsMargin,
                rectBottom
            )
            Result.DRAW -> RectF(
                right - itemWidth * 3 - headerBtwResultsMargin * 2,
                rectTop,
                right - itemWidth * 2 - headerBtwResultsMargin * 2,
                rectBottom
            )
        }

        drawRoundRect(
            rect,
            rectRadius,
            rectRadius,
            rectPaint
        )

        val textPaint = getResultTextPaint(result)
        val score = when (result) {
            Result.WIN -> monthResult.won
            Result.LOSE -> monthResult.lost
            Result.DRAW -> monthResult.draw
        }.run {
            val isNotSingleDigitScore = this > 9
            if (isNotSingleDigitScore) {
                this.toString()
            } else {
                "0$this"
            }
        }

        val fontFeatureMargin = 2
        drawText(
            score,
            rect.centerX(),
            rect.centerY() + textPaint.fontMetrics.bottom + fontFeatureMargin,
            textPaint
        )
    }

    private fun getResultPaint(result: Result): Paint {
        return when (result) {
            Result.WIN -> rectPaint.apply { color = winColor }
            Result.LOSE -> rectPaint.apply { color = loseColor }
            Result.DRAW -> rectPaint.apply { color = drawColor }
        }
    }

    private fun getResultTextPaint(result: Result): Paint {
        return when (result) {
            Result.WIN -> valueTextPaint.apply { color = winTextColor }
            Result.LOSE -> valueTextPaint.apply { color = loseTextColor }
            Result.DRAW -> valueTextPaint.apply { color = drawTextColor }
        }
    }

    private enum class Result {
        WIN,
        LOSE,
        DRAW
    }
}