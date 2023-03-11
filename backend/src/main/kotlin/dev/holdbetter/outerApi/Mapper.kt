package dev.holdbetter.outerApi

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.outerApi.model.LivescoreDataResponse

internal object Mapper {
    fun LivescoreDataResponse.getTeams(): List<TeamRankDTO> =
        standings.map {
            TeamRankDTO(
                id = it.id,
                rank = it.rank,
                name = it.name,
                image = it.image,
                gamePlayed = it.gamePlayed,
                points = it.points,
                wins = it.wins,
                loses = it.loses,
                draws = it.draws,
                goalsFor = it.goalsFor,
                goalsAgainst = it.goalsAgainst,
                goalsDiff = it.goalsDiff,
                alterImageId = null
            )
        }

    fun LivescoreDataResponse.getMatches(): List<MatchdayDTO> =
        matches.map {
            MatchdayDTO(
                id = it.id,
                resultHome = it.resultHome,
                resultAway = it.resultAway,
                teamHomeId = it.teamHomeId,
                teamAwayId = it.teamAwayId,
                status = it.status,
                statusId = it.statusId,
                whoWon = it.whoWon,
                startDate = it.startDate,
                endDate = it.endDate
            )
        }
}