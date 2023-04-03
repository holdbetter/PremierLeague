package dev.holdbetter.shared.feature_team_detail_impl

import androidx.recyclerview.widget.DiffUtil
import dev.holdbetter.shared.feature_team_detail.DateHolder
import dev.holdbetter.shared.feature_team_detail_impl.DateAdapter.Companion.COLORED_PAYLOAD
import dev.holdbetter.shared.feature_team_detail_impl.DateAdapter.Companion.SELECTED_PAYLOAD

internal object DateDiffer : DiffUtil.ItemCallback<DateHolder>() {

    override fun areItemsTheSame(
        oldItem: DateHolder,
        newItem: DateHolder
    ): Boolean {
        return true
    }

    override fun areContentsTheSame(
        oldItem: DateHolder,
        newItem: DateHolder
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: DateHolder, newItem: DateHolder): Any? {
        return when {
            oldItem.isSelected != newItem.isSelected -> SELECTED_PAYLOAD
            oldItem.isColored != newItem.isColored -> COLORED_PAYLOAD
            else -> null
        }
    }
}