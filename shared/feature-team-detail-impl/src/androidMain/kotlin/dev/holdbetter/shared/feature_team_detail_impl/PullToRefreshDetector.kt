package dev.holdbetter.shared.feature_team_detail_impl

import android.view.MotionEvent
import kotlin.math.abs
import kotlin.math.min

internal class PullToRefreshDetector(
    refresherSize: Int,
    refreshMarginBottom: Int,
    pullOffset: Int,
    private val onPullAction: (Float) -> Unit,
    private val onRefreshAction: (Boolean, Float) -> Unit,
    private val resetAction: () -> Unit
) {

    private val maxPullOffset = pullOffset
    private val refresherMarginVertical = refreshMarginBottom * 2f
    private val maxPullDistance = maxPullOffset + refresherSize + refreshMarginBottom * 1f
    private val refreshDistance = refresherSize + refresherMarginVertical

    private var scrollStartPointY: Float? = null
    private var pullDistance: Float = 0f

    private val isPullToRefreshActive
        get() = pullDistance != 0f

    fun onScroll(
        startPoint: MotionEvent?,
        currentPoint: MotionEvent?,
    ) {
        if (startPoint == null || currentPoint == null) {
            return
        }

        scrollStartPointY = startPoint.y

        val scrollDirection = if (startPoint.y <= currentPoint.y) {
            ScrollDirection.TOP
        } else {
            ScrollDirection.BOTTOM
        }

        when (scrollDirection) {
            ScrollDirection.TOP -> pull(startPoint.y, currentPoint.y)
            ScrollDirection.BOTTOM -> if (isPullToRefreshActive) pull(startPoint.y, currentPoint.y)
        }
    }

    fun onScrollUp(e: MotionEvent?) {
        if (e == null) {
            return
        }

        scrollStartPointY?.let {
            if (pullDistance > maxPullDistance * .8) {
                onRefreshAction(true, refreshDistance * 1f)
            } else {
                onRefreshAction(false, 0f)
            }
        }.also {
            scrollStartPointY = null
            pullDistance = 0f
        }
    }

    fun reset() {
        resetAction()
    }

    private fun pull(startY: Float, currentY: Float) {
        val scrollDistanceY = abs(startY - currentY) / 4f
        pullDistance = min(scrollDistanceY, maxPullDistance)
        onPullAction(pullDistance)
    }

    private enum class ScrollDirection {
        TOP,
        BOTTOM
    }
}