package dev.holdbetter.feature_standings_impl.domain

import dev.holdbetter.common.TeamRankDTO
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
            goalsDiff = goalsDiff
        )
    }
}

internal fun StandingsView.Event.toIntent() =
    when(this) {
        is StandingsView.Event.TeamSelected -> StandingsStore.Intent.OpenTeamDetail(teamId)
        StandingsView.Event.Reload -> StandingsStore.Intent.Reload
    }

internal fun StandingsStore.State.toModel() =
    when (data) {
        is StandingsStore.State.Data.Error -> {
            StandingsView.Model(
                isLoading = isLoading,
                isRefreshEnabled = isRefreshEnabled,
                isError = true,
                standings = null
            )
        }
        is StandingsStore.State.Data.Standings -> {
            StandingsView.Model(
                isLoading = isLoading,
                isRefreshEnabled = isRefreshEnabled,
                isError = false,
                standings = data as StandingsStore.State.Data.Standings
            )
        }
        null -> {
            StandingsView.Model(
                isLoading = isLoading,
                isRefreshEnabled = isRefreshEnabled,
                isError = false,
                standings = null
            )
        }
    }