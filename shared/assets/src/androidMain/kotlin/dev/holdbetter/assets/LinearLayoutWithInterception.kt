package dev.holdbetter.assets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

class LinearLayoutWithInterception @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defAttrs: Int = 0
) : LinearLayout(context, attributeSet, defAttrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}