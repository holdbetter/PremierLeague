package dev.holdbetter.database

import dev.holdbetter.database.entity.DayLimit
import dev.holdbetter.database.entity.Match
import dev.holdbetter.database.entity.Team
import dev.holdbetter.innerApi.model.Limit
import dev.holdbetter.innerApi.model.Matchday
import dev.holdbetter.innerApi.model.TeamRank
import dev.holdbetter.innerApi.model.TeamWithMatches

internal object Mapper {

    fun toModel(match: Match): Matchday =
        with(match) {
            Matchday(
                id = id.value,
                resultHome = resultHome,
                resultAway = resultAway,
                teamHomeId = teamHomeId.value,
                teamAwayId = teamAwayId.value,
                status = status,
                statusId = statusId,
                whoWon = whoWon,
                startDate = startDate,
                endDate = endDate,
                teamHome = toModel(teamHome),
                teamAway = toModel(teamAway)
            )
        }

    fun toModel(team: Team): TeamRank {
        return TeamRank(
            team.teamId.value,
            team.rank,
            team.name,
            team.image,
            team.gamePlayed,
            team.points,
            team.wins,
            team.loses,
            team.draws,
            team.goalsFor,
            team.goalsAgainst,
            team.goalsDiff
        )
    }

    fun toModel(dayLimit: DayLimit): Limit {
        return with(dayLimit) {
            Limit(
                gameDayDuration = this.gameDayDuration,
                plannedDayLimit = this.plannedDayLimit,
                firstMatchStartOrDefault = this.firstMatchStartOrDefault,
                remainedDayLimit = this.remainedDayLimit,
                updateRate = this.updateRate
            )
        }
    }

    fun Team.toTeamWithMatches(): TeamWithMatches {
        return TeamWithMatches(
            teamRank = TeamRank(
                id = id.value,
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
            ),
            teamMatches = homeMatches.map(::toModel) + awayMatches.map(::toModel)
        )
    }
}