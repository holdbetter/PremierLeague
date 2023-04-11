package dev.holdbetter.shared.feature_team_detail_impl

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import java.security.InvalidParameterException

internal class GroupRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    override fun onTouchEvent(e: MotionEvent?) = false

    override fun setLayoutManager(layout: LayoutManager?) {
        if (!isInEditMode) {
            if (layout != null && layout !is GroupLayoutManager) {
                throw InvalidParameterException("GroupLayoutManager only possible")
            }
        }

        super.setLayoutManager(layout)
    }

    override fun addItemDecoration(decor: ItemDecoration) {
        if (!isInEditMode) {
            if (itemDecorationCount == 0) {
                if (decor !is GroupGridDecorator) {
                    throw InvalidParameterException("GroupGridDecorator required first")
                }
            }
        }

        super.addItemDecoration(decor)
    }

    override fun addItemDecoration(decor: ItemDecoration, index: Int) {
        if (!isInEditMode) {
            if (itemDecorationCount == 0) {
                if (decor !is GroupGridDecorator) {
                    throw InvalidParameterException("GroupGridDecorator required first")
                }
            }
        }

        super.addItemDecoration(decor, index)
    }
}