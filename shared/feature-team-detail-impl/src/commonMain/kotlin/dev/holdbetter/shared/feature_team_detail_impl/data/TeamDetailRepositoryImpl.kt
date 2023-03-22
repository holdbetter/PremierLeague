package dev.holdbetter.shared.feature_team_detail_impl.data

import dev.holdbetter.common.Status
import dev.holdbetter.core_network.model.TeamId
import dev.holdbetter.shared.feature_team_detail.Match
import dev.holdbetter.shared.feature_team_detail.MonthResult
import dev.holdbetter.shared.feature_team_detail.TeamDetailRepository
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore
import dev.holdbetter.shared.feature_team_detail_impl.domain.Mapper

internal class TeamDetailRepositoryImpl(
    private val teamDetailDataSource: TeamDetailDataSource
) : TeamDetailRepository {

    override suspend fun getTeamDetail(teamId: Long): TeamDetailStore.State.Data.TeamDetail {
        val id = teamId.toString()
        val teamWithMatches = teamDetailDataSource.getTeamWithMatches(TeamId(id))
        val team = Mapper.dtoToState(teamWithMatches.teamRank)
        val allMatches = teamWithMatches.teamMatches.map(Mapper::dtoToState)

        val pastMatchesByMonth = allMatches.filter { it.statusId == Status.FULL_TIME.id }
            .sortedByDescending { requireNotNull(it.startDate) }
            .groupBy { requireNotNull(it.startDate).month }
            .mapValues {
                val (won, lost, draw) = countMonthResult(id, it.value)
                MonthResult(
                    won = won,
                    lost = lost,
                    draw = draw,
                    matches = it.value
                )
            }

        return TeamDetailStore.State.Data.TeamDetail(
            team,
            allMatches,
            pastMatchesByMonth
        )
    }

    private fun countMonthResult(teamId: String, monthMatches: List<Match>): Triple<Int, Int, Int> {
        var won = 0
        var lost = 0
        var draw = 0

        for (match in monthMatches) {
            val isHome = teamId == match.teamHomeId
            when (match.whoWon) {
                0 -> draw++
                1 -> if (isHome) won++ else lost++
                2 -> if (!isHome) won++ else lost++
            }
        }

        return Triple(won, lost, draw)
    }
}