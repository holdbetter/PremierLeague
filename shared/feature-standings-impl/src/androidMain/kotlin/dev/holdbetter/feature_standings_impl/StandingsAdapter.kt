package dev.holdbetter.feature_standings_impl

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.holdbetter.assets.assetsDrawable
import dev.holdbetter.assets.loadWithPlaceholder
import dev.holdbetter.feature_standings_api.StandingsStore.State.Data.Standings.TeamRank
import dev.holdbetter.feature_standings_impl.databinding.TeamRankBinding
import dev.holdbetter.shared.core_database.api.FavoritesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class StandingsAdapter(
    private val lifecycleScope: CoroutineScope,
    private val favoritesApi: FavoritesApi,
    private val favoriteDrawable: Drawable?,
    private val onItemClickAction: (teamId: Long) -> Unit
) : ListAdapter<TeamRank, StandingsAdapter.TeamRankVH>(TeamRankDiffer()) {

    var userFavoriteTeams: List<Long> = emptyList()

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

        fun bind(teamRank: TeamRank) {
            with(binding) {
                clear(logo)
                rank.text = teamRank.rank.toString()
                decorateRankWithFavoriteIcon(teamRank.id.toLong())
                logo.loadWithPlaceholder(teamRank.image, assetsDrawable.league_logo_mini)
                team.text = teamRank.name
                points.text = teamRank.points.toString()
                matches.text = teamRank.gamePlayed.toString()

                root.setOnClickListener { onItemClickAction(teamRank.id.toLong()) }
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
    }

    class TeamRankDiffer : DiffUtil.ItemCallback<TeamRank>() {
        override fun areItemsTheSame(oldItem: TeamRank, newItem: TeamRank): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TeamRank, newItem: TeamRank): Boolean {
            return oldItem == newItem
        }
    }
}