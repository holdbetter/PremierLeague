package dev.holdbetter.feature_standings_impl.domain

import dev.holdbetter.common.LeagueDTO
import dev.holdbetter.common.TeamDTO
import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.feature_standings_api.StandingsView

internal object Mapper {
    fun mapDtoToState(leagueDTO: LeagueDTO): StandingsStore.State.Data.Standings =
        with(leagueDTO) {
            StandingsStore.State.Data.Standings(
                name = name,
                country = country,
                logoUrl = logo,
                teams = mapStandingsToTeamRanks(standings)
            )
        }

    private fun mapStandingsToTeamRanks(standings: List<LeagueDTO.StandingDTO>) =
        standings.map(Mapper::standingToTeamTank)

    private fun standingToTeamTank(standingDTO: LeagueDTO.StandingDTO) =
        with(standingDTO) {
            StandingsStore.State.Data.Standings.TeamRank(
                rank = rank,
                points = points,
                team = mapStandingToTeam(team),
                allStats = mapStatsDtoToStats(stats),
                homeStats = mapStatsDtoToStats(homeStats),
                awayStats = mapStatsDtoToStats(awayStats),
                update = update
            )
        }

    private fun mapStandingToTeam(teamDTO: TeamDTO) =
        with(teamDTO) {
            StandingsStore.State.Data.Standings.Team(
                id = id,
                name = name,
                logoUrl = logo
            )
        }

    private fun mapStatsDtoToStats(statsDTO: LeagueDTO.StatsDTO) =
        with(statsDTO) {
            StandingsStore.State.Data.Standings.Stats(
                played = played,
                win = win,
                draw = draw,
                lose = lose,
                goals = mapGoalsDtoToGoals(goals)
            )
        }

    private fun mapGoalsDtoToGoals(goalsDTO: LeagueDTO.GoalsDTO) =
        with(goalsDTO) {
            StandingsStore.State.Data.Standings.Goals(
                goalsFor = goalsFor,
                against = against
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