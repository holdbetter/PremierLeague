package dev.holdbetter.outerApi

import dev.holdbetter.innerApi.model.Matchday
import dev.holdbetter.innerApi.model.TeamRank
import dev.holdbetter.outerApi.model.LivescoreDataResponse

internal object Mapper {
    fun LivescoreDataResponse.getTeams(): List<TeamRank> =
        standings.map {
            TeamRank(
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
                goalsDiff = it.goalsDiff
            )
        }

    fun LivescoreDataResponse.getMatches(): List<Matchday> =
        matches.map {
            Matchday(
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