package dev.holdbetter.feature_standings_impl

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import dev.holdbetter.assets.HorizontalMarginDecorator
import dev.holdbetter.assets.assetsDrawable
import dev.holdbetter.assets.loadWithPlaceholder
import dev.holdbetter.assets.px
import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.feature_standings_api.StandingsStore.State.Data.Standings.TeamRank
import dev.holdbetter.feature_standings_impl.databinding.TeamRankBinding
import dev.holdbetter.shared.core_database.api.FavoritesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class StandingsAdapter(
    context: Context,
    private val lifecycleScope: CoroutineScope,
    private val favoritesApi: FavoritesApi,
    private val favoriteDrawable: Drawable?,
    private val onItemClickAction: (teamId: Long) -> Unit
) : ListAdapter<TeamRank, StandingsAdapter.TeamRankVH>(TeamRankDiffer) {

    private var userFavoriteTeams: List<Long> = emptyList()

    private val winLiveColor = context.getColor(R.color.live_win)
    private val loseLiveColor = context.getColor(R.color.live_lose)
    private val drawLiveColor = context.getColor(R.color.live_draw)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamRankVH {
        return TeamRankVH(
            TeamRankBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClickAction
        )
    }

    override fun onBindViewHolder(holder: TeamRankVH, position: Int) {
        holder.bind(currentList[position])
    }

    fun submitData(list: List<TeamRank>?) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                this@StandingsAdapter.userFavoriteTeams = favoritesApi.getFavoriteTeamIds()
            }
            submitList(list)
        }
    }

    inner class TeamRankVH(
        private val binding: TeamRankBinding,
        private val onItemClickAction: (teamId: Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val liveAnimatedDrawable = AnimatedVectorDrawableCompat.create(
            itemView.context,
            R.drawable.anim_live_indicator
        )

        init {
            binding.lastResults.adapter = LastResultAdapter(itemView.context)
            binding.lastResults.addItemDecoration(HorizontalMarginDecorator(4.px.toInt()))

            liveAnimatedDrawable?.registerAnimationCallback(object :
                Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    binding.playingStatus.post { liveAnimatedDrawable.start() }
                }
            })
        }

        fun bind(teamRank: TeamRank) {
            with(binding) {
                clear(logo)
                rank.text = teamRank.rank.toString()
                decorateRankWithFavoriteIcon(teamRank.id.toLong())
                logo.loadWithPlaceholder(teamRank.image, assetsDrawable.league_logo_mini)
                team.text = teamRank.name
                points.text = teamRank.points.toString()
                matches.text = teamRank.gamePlayed.toString()

                bindResults(teamRank)

                root.setOnClickListener { onItemClickAction(teamRank.id.toLong()) }
            }
        }

        private fun bindResults(teamRank: TeamRank) {
            with(binding) {
                playingStatus.isInvisible = teamRank.liveMatch == null

                playingStatus.setImageDrawable(liveAnimatedDrawable)
                playingStatus.imageTintList = teamRank.liveMatch?.let {
                    chooseLiveIndicatorColor(teamRank.id, it)
                }

                teamRank.liveMatch?.let { liveAnimatedDrawable?.start() }

                (lastResults.adapter as? LastResultAdapter)?.submitData(teamRank.lastResults)
            }
        }

        private fun clear(view: View) {
            Glide.with(view.context)
                .clear(view)
        }

        private fun decorateRankWithFavoriteIcon(teamId: Long) {
            if (userFavoriteTeams.contains(teamId)) {
                binding.rank.setCompoundDrawables(null, null, null, favoriteDrawable)
            } else {
                binding.rank.setCompoundDrawables(null, null, null, null)
            }
        }

        private fun chooseLiveIndicatorColor(
            teamId: String,
            matchdayDTO: MatchdayDTO
        ): ColorStateList {
            val isHomeMatch = teamId == matchdayDTO.teamHomeId
            val scoreDiff = matchdayDTO.resultHome.toInt() - matchdayDTO.resultAway.toInt()
            val validDiff = if (isHomeMatch) scoreDiff else -scoreDiff
            return ColorStateList.valueOf(diffToColor(validDiff))
        }

        private fun diffToColor(diff: Int): Int {
            return when {
                diff == 0 -> drawLiveColor
                diff > 0 -> winLiveColor
                else -> loseLiveColor
            }
        }
    }

    private object TeamRankDiffer : DiffUtil.ItemCallback<TeamRank>() {
        override fun areItemsTheSame(oldItem: TeamRank, newItem: TeamRank): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TeamRank, newItem: TeamRank): Boolean {
            return oldItem == newItem
        }
    }
}