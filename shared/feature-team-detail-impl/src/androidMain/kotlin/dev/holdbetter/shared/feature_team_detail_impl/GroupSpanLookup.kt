package dev.holdbetter.shared.feature_team_detail_impl

import androidx.recyclerview.widget.GridLayoutManager

// TODO: Test
// TODO: Can be unstable
// TODO: Empty list, single item list checks
internal class GroupSpanLookup(
    private val spanCount: Int,
    private val matchesAdapter: MatchesAdapter
) : GridLayoutManager.SpanSizeLookup() {

    private val spans = mutableListOf<Int>()
    private val lastRowInGroup = mutableListOf<Int>()
    private val firstItemInGroup = mutableListOf<Int>()
    private val spanSizes = mutableMapOf<Int, Int>()

    init {
        isSpanGroupIndexCacheEnabled = false
    }

    override fun getSpanIndex(position: Int, spanCount: Int): Int {
        var sum = 0
        for (i in 0 until position) {
            sum += spanSizes.getOrDefault(i, 0)
        }
        return sum % spanCount
    }

    override fun getSpanSize(position: Int): Int {
        val cachedSize = spanSizes[position]
        return cachedSize ?: computeSpanSize(position)
    }

    override fun invalidateSpanIndexCache() {
        clear()
    }

    fun isItemInLastRowOfGroup(position: Int) = lastRowInGroup.contains(position)

    fun isFirstItemInGroup(position: Int) = firstItemInGroup.contains(position)

    private fun clear() {
        spans.clear()
        lastRowInGroup.clear()
        firstItemInGroup.clear()
        spanSizes.clear()
    }

    // TODO: Test
    private fun computeSpanSize(position: Int): Int {
        if (spans.sum() == spanCount) {
            spans.clear()
        }

        val isLastMatch = matchesAdapter.lastMatchIndexes.contains(position)
        spanSizes[position] = if (isLastMatch) {
            val span = when (val sum: Int = spans.sum()) {
                0 -> spanCount
                else -> spanCount - sum
            }
            spans.add(span)
            addFirstItemIndexOfGroup(position)
            addToLastRowInGroup(position, span)
            span
        } else {
            spans.add(1)
            1
        }

        return spanSizes.getOrDefault(position, 1)
    }

    private fun addFirstItemIndexOfGroup(lastPosition: Int) {
        if (firstItemInGroup.isEmpty()) {
            firstItemInGroup.add(0)
        }

        val lastItemCountIndex = matchesAdapter.itemCount - 1
        val nextPossibleGroupFirstItemIndex = lastPosition + 1
        if (nextPossibleGroupFirstItemIndex <= lastItemCountIndex) {
            firstItemInGroup.add(nextPossibleGroupFirstItemIndex)
        }
    }

    private fun addToLastRowInGroup(position: Int, span: Int) {
        when (span) {
            3 -> lastRowInGroup.add(position)
            2 -> lastRowInGroup.addAll(arrayOf(position, position - 1))
            1 -> lastRowInGroup.addAll(arrayOf(position, position - 1, position - 2))
        }
    }
}