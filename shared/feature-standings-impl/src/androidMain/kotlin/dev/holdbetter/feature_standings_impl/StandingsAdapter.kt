package dev.holdbetter.feature_standings_impl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.holdbetter.assets.loadWithPlaceholder
import dev.holdbetter.feature_standings_api.StandingsStore.State.Data.Standings.TeamRank
import dev.holdbetter.feature_standings_impl.databinding.TeamRankBinding

internal class StandingsAdapter :
    ListAdapter<TeamRank, StandingsAdapter.TeamRankVH>(TeamRankDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamRankVH {
        return TeamRankVH(
            TeamRankBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TeamRankVH, position: Int) {
        holder.bind(currentList[position])
    }

    class TeamRankVH(
        private val binding: TeamRankBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamRank: TeamRank) {
            with(binding) {
                clear(logo)
                rank.text = teamRank.rank.toString()
                logo.loadWithPlaceholder(teamRank.team.logoUrl, R.drawable.league_logo_mini)
                team.text = teamRank.team.name
                points.text = teamRank.points.toString()
            }
        }

        private fun clear(view: View) {
            Glide.with(view.context)
                .clear(view)
        }
    }

    class TeamRankDiffer : DiffUtil.ItemCallback<TeamRank>() {
        override fun areItemsTheSame(oldItem: TeamRank, newItem: TeamRank): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TeamRank, newItem: TeamRank): Boolean {
            return oldItem == newItem
        }
    }
}