package dev.holdbetter.shared.feature_team_detail_impl

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

// For support not lnum fontFeature and avoid bad fontPadding you have to override textHeight (bounds)
internal class CustomTypefaceSpan(
    private val font: Typeface?,
    private val size: Float,
    private val isSameNumbers: Boolean
) : MetricAffectingSpan() {

    override fun updateDrawState(tp: TextPaint?) {
        tp?.applyTypeface()
    }

    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.applyTypeface()
    }

    private fun TextPaint.applyTypeface() {
        typeface = font
        textSize = size
        if (isSameNumbers) {
            fontFeatureSettings = "lnum"
        }
    }
}