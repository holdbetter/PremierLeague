package dev.holdbetter.shared.feature_team_detail_impl

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.holdbetter.assets.load
import dev.holdbetter.shared.feature_team_detail.Match
import dev.holdbetter.shared.feature_team_detail.MonthResult
import dev.holdbetter.shared.feature_team_detail_impl.MatchesAdapter.MatchViewHolder
import dev.holdbetter.shared.feature_team_detail_impl.databinding.MatchItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Month
import kotlin.properties.Delegates

internal class MatchesAdapter(
    private val lifecycleScope: CoroutineScope,
    private val teamId: Int,
    val isDarkMode: Boolean,
    context: Context
) : ListAdapter<Match, MatchViewHolder>(MatchDiffer) {

    val winColor = context.getColor(R.color.result_match_corner_win)
    val drawColor = context.getColor(R.color.result_match_corner_draw)
    val loseColor = context.getColor(R.color.result_match_corner_lose)
    val startDayColor = context.getColor(R.color.match_card_default_start_color)
    val endDayColor = context.getColor(R.color.match_card_default_end_color)

    var startNightColor by Delegates.notNull<Int>()
    var endNightColor by Delegates.notNull<Int>()

    var lastMatchIndexes: List<Int> = listOf()
        private set

    private var groupedMatches: Map<Month, MonthResult> = emptyMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        return MatchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.match_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount() = currentList.count()

    fun submitData(@ColorInt teamColor: Int, groupedMatches: Map<Month, MonthResult>) {
        this.groupedMatches = groupedMatches
        lifecycleScope.launch {
            val matchListToBeDisplayed: List<Match>

            withContext(Dispatchers.IO) {
                initTeamColors(teamColor)

                val lastMatchesInMonth = groupedMatches.values.map {
                    it.matches.last()
                }

                matchListToBeDisplayed = groupedMatches.values
                    .map(MonthResult::matches)
                    .flatten()

                lastMatchIndexes = matchListToBeDisplayed.mapIndexed { index, match -> index to match }
                        .filter { lastMatchesInMonth.contains(it.second) }
                        .map { it.first }
            }

            submitList(matchListToBeDisplayed)
        }
    }

    fun getMonthResult(month: Month): MonthResult {
        return groupedMatches.getValue(month)
    }

    private fun initTeamColors(teamColor: Int) {
        val hsl = floatArrayOf(0f, 0f, 0f)
        ColorUtils.colorToHSL(teamColor, hsl)
        val (hue, sat, lum) = hsl
        hsl[1] = sat * .9f
        hsl[2] = lum * 1.05f

        this.startNightColor = ColorUtils.HSLToColor(hsl)

        hsl[1] = sat * .7f
        hsl[2] = lum * .5f
        this.endNightColor = ColorUtils.HSLToColor(hsl)
    }

    inner class MatchViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private val binding = MatchItemBinding.bind(view)

        init {
            view.clipToOutline = true
            view.setGradientColor(isDarkMode)
        }

        fun bind(match: Match) {
            with(binding) {
                clear(homeImage)
                match.teamHome?.image?.let(homeImage::load)

                clear(awayImage)
                match.teamAway?.image?.let(awayImage::load)

                homeScore.text = match.resultHome
                awayScore.text = match.resultAway

                dayDate.text = match.startDate?.dayOfMonth.toString()

                cardResultIndicator.imageTintList = ColorStateList.valueOf(
                    getCornerColor(match.teamHomeId.toInt(), match.whoWon)
                )
            }
        }

        private fun clear(view: View) {
            Glide.with(view.context)
                .clear(view)
        }

        @ColorInt
        private fun getCornerColor(matchHomeId: Int, whoWon: Int): Int {
            val isHome = teamId == matchHomeId
            return when (whoWon) {
                0 -> drawColor
                1 -> if (isHome) winColor else loseColor
                2 -> if (!isHome) winColor else loseColor
                else -> drawColor
            }
        }

        private fun View.setGradientColor(isDarkMode: Boolean) {
            val gradient = background as GradientDrawable
            gradient.colors = if (isDarkMode) {
                intArrayOf(
                    startNightColor,
                    endNightColor
                )
            } else {
                intArrayOf(
                    startDayColor,
                    endDayColor
                )
            }
        }
    }

    private object MatchDiffer : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(oldItem: Match, newItem: Match) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Match, newItem: Match) = oldItem == newItem
    }
}