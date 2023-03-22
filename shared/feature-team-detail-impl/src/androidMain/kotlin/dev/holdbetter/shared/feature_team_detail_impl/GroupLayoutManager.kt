package dev.holdbetter.shared.feature_team_detail_impl

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

internal class GroupLayoutManager(
    context: Context,
    matchesAdapter: MatchesAdapter,
    spanCount: Int
) : GridLayoutManager(
    context,
    spanCount,
    VERTICAL,
    false
) {
    init {
        spanSizeLookup = GroupSpanLookup(spanCount, matchesAdapter)
    }
}