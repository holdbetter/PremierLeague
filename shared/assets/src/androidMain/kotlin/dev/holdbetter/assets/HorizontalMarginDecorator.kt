package dev.holdbetter.assets

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class HorizontalMarginDecorator(@Px private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemIndex = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        outRect.left = itemIndex * spacing / itemCount
        outRect.right = spacing - (itemIndex + 1) * spacing / itemCount
    }
}