package dev.holdbetter.feature_standings_impl.domain

import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.feature_standings_api.SelectedTeam
import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.feature_standings_api.StandingsView

internal object Mapper {
    fun toState(standings: List<TeamRankDTO>): StandingsStore.State.Data.Standings =
        StandingsStore.State.Data.Standings(
            teams = standings.map(::dtoToState)
        )

    private fun dtoToState(teamRank: TeamRankDTO) = with(teamRank) {
        StandingsStore.State.Data.Standings.TeamRank(
            id = id,
            rank = rank,
            name = name,
            image = image,
            gamePlayed = gamePlayed,
            points = points,
            wins = wins,
            loses = loses,
            draws = draws,
            goalsFor = goalsFor,
            goalsAgainst = goalsAgainst,
            goalsDiff = goalsDiff,
            lastResults = lastFiveResults,
            liveMatch = liveMatch
        )
    }
}

internal fun StandingsView.Event.toIntent() =
    when(this) {
        is StandingsView.Event.TeamSelected -> StandingsStore.Intent.OpenTeamDetail(teamId)
        StandingsView.Event.Reload -> StandingsStore.Intent.Reload
        StandingsView.Event.NavigationCommit -> StandingsStore.Intent.NavigationCommit
    }

internal fun StandingsStore.State.toModel() = StandingsView.Model(
    isLoading = isLoading,
    isRefreshEnabled = isRefreshEnabled,
    isError = data != null && data is Throwable,
    standings = data as? StandingsStore.State.Data.Standings,
    selectedTeam = selectedTeam?.run {
        SelectedTeam(
            id.toLong(),
            image
        )
    }
)