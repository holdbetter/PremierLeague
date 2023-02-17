package dev.holdbetter.feature_standings_impl

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

internal class StandingsDecorator(
    context: Context
) : ItemDecoration() {

    private val divider = ContextCompat.getDrawable(context, R.drawable.standings_separator)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount - 1) {
            with(parent[i]) {
                divider?.setBounds(left, bottom - divider.intrinsicHeight, right, bottom)
                divider?.draw(c)
            }
        }
    }
}