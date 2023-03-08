package dev.holdbetter.database

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.common.TeamWithMatchesDTO
import dev.holdbetter.core_network.model.RemoteLivescoreConfig
import dev.holdbetter.database.entity.DayLimit
import dev.holdbetter.database.entity.Match
import dev.holdbetter.database.entity.Team
import dev.holdbetter.innerApi.model.Limit

internal object Mapper {

    fun toModel(match: Match): MatchdayDTO =
        with(match) {
            MatchdayDTO(
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

    fun toModel(team: Team): TeamRankDTO {
        return TeamRankDTO(
            id = team.teamId.value,
            rank = team.rank,
            name = team.name,
            image = "${RemoteLivescoreConfig.IMAGE_HOST}${team.image}",
            gamePlayed = team.gamePlayed,
            points = team.points,
            wins = team.wins,
            loses = team.loses,
            draws = team.draws,
            goalsFor = team.goalsFor,
            goalsAgainst = team.goalsAgainst,
            goalsDiff = team.goalsDiff
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

    fun Team.toTeamWithMatches(): TeamWithMatchesDTO {
        return TeamWithMatchesDTO(
            teamRank = TeamRankDTO(
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