package dev.holdbetter.shared.feature_team_detail_impl

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import dev.holdbetter.shared.feature_team_detail.DateHolder

internal class OffsetAsyncListDiffer(
    adapter: DateAdapter,
    private val listUpdateCallback: DateUpdateCallback = DateUpdateCallback(adapter)
) : AsyncListDiffer<DateHolder>(
    listUpdateCallback,
    AsyncDifferConfig.Builder(DateDiffer).build()
) {

    fun submitData(offset: Int, newList: List<DateHolder>) {
        listUpdateCallback.offset = offset
        submitList(newList)
    }
}