package dev.holdbetter.shared.feature_team_detail_impl

import androidx.recyclerview.widget.ListUpdateCallback

// onChanged only tested
internal class DateUpdateCallback(
    private val adapter: DateAdapter
) : ListUpdateCallback {

    var offset = 0

    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemInserted(position)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        val realPosition = position + offset
        adapter.notifyItemRangeChanged(realPosition, count, payload)
    }
}