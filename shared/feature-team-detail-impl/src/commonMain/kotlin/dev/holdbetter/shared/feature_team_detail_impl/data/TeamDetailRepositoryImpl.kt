package dev.holdbetter.shared.feature_team_detail_impl.data

import dev.holdbetter.common.Status
import dev.holdbetter.common.util.isGameOver
import dev.holdbetter.common.util.isRunning
import dev.holdbetter.core_di_api.folder.Dikt
import dev.holdbetter.core_network.model.TeamId
import dev.holdbetter.shared.feature_team_detail.Match
import dev.holdbetter.shared.feature_team_detail.MonthResult
import dev.holdbetter.shared.feature_team_detail.TeamDetailRepository
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore
import dev.holdbetter.shared.feature_team_detail.initCalendar
import dev.holdbetter.shared.feature_team_detail_impl.domain.Mapper
import dev.shustoff.dikt.InjectableSingleInScope

internal class TeamDetailRepositoryImpl(
    private val teamDetailDataSource: TeamDetailDataSource
) : TeamDetailRepository, InjectableSingleInScope<Dikt> {

    override suspend fun getTeamDetail(teamId: Long): TeamDetailStore.State.Data.TeamDetail {
        val id = teamId.toString()
        val teamWithMatches = teamDetailDataSource.getTeamWithMatches(TeamId(id))
        val team = Mapper.dtoToState(teamWithMatches.teamRank)
        val allMatches = teamWithMatches.teamMatches.map(Mapper::dtoToState)

        val pastMatchesByMonth = allMatches.filter { it.statusId.isGameOver }
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

        val calendar = initCalendar().run {
            val matchDates = allMatches.map { it.startDate?.date }
            map { if (matchDates.contains(it.date)) it.copy(isColored = true) else it }
        }

        val nearMatch = findNearMatch(allMatches)
        return TeamDetailStore.State.Data.TeamDetail(
            team = team,
            allMatches = allMatches,
            pastResultsByMonth = pastMatchesByMonth,
            isTeamFavorite = teamDetailDataSource.isTeamFavorite(teamId),
            matchCard = nearMatch,
            nextMatch = nearMatch,
            calendar = calendar,
            isCompareAvailable = teamWithMatches.isCompareFeatureAvailable
        )
    }

    override suspend fun changeTeamFavorite(teamId: Long): Boolean {
        return if (!teamDetailDataSource.isTeamFavorite(teamId)) {
            teamDetailDataSource.addFavoriteTeam(teamId)
            true
        } else {
            teamDetailDataSource.removeFavoriteTeam(teamId)
            false
        }
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

    override fun findNearMatch(matches: List<Match>): Match {
        val sortedMatches = matches.sortedBy(Match::startDate)
        return sortedMatches.firstOrNull { match ->
            match.statusId.isRunning ||
                    match.statusId == Status.NOT_STARTED.id
        }
            ?: sortedMatches.last()
    }
}