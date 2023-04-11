package dev.holdbetter.shared.feature_team_detail_impl

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.annotation.Px
import dev.holdbetter.assets.px
import kotlin.math.abs

internal class ScrollViewTouchListener(
    @Px refresherSize: Int,
    @Px refreshMarginBottom: Int,
    @Px pullOffset: Int,
    onPullAction: (Float) -> Unit,
    onRefreshAction: (Boolean, Float) -> Unit,
    resetRefresh: () -> Unit
) : OnTouchListener {

    private val slop = 10.px

    private val pullToRefreshDetector = PullToRefreshDetector(
        refresherSize = refresherSize,
        refreshMarginBottom = refreshMarginBottom,
        pullOffset = pullOffset,
        onPullAction = onPullAction,
        onRefreshAction = onRefreshAction,
        resetAction = resetRefresh
    )

    private var scrollStartEvent: MotionEvent? = null
    private var lastScrollEvent: MotionEvent? = null

    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        return if (v.scrollY == 0) {
            when (event?.actionMasked) {
                MotionEvent.ACTION_UP -> {
                    pullToRefreshDetector.onScrollUp(event)
                    scrollStartEvent = null
                    lastScrollEvent = null
                    false
                }
                MotionEvent.ACTION_MOVE -> {
                    val scrollStart = scrollStartEvent
                    val lastScroll = lastScrollEvent
                    if (scrollStart != null && lastScroll != null) {
                        if (!isSlopMove(scrollStart, lastScroll, event)) {
                            pullToRefreshDetector.onScroll(scrollStartEvent, event)
                        }
                    }
                    lastScrollEvent = MotionEvent.obtain(event)
                    false
                }
                MotionEvent.ACTION_DOWN -> {
                    scrollStartEvent = MotionEvent.obtain(event)
                    false
                }
                else -> false
            }
        } else {
            scrollStartEvent = null
            lastScrollEvent = null
            pullToRefreshDetector.reset()
            false
        }
    }

    private fun isSlopMove(
        scrollStartEvent: MotionEvent,
        lastScrollEvent: MotionEvent,
        currentScrollEvent: MotionEvent
    ): Boolean {
        return if (lastScrollEvent.y > currentScrollEvent.y) {
            false
        } else {
            abs(scrollStartEvent.y - currentScrollEvent.y) < slop
        }
    }
}