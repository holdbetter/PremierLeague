package dev.holdbetter.database

import dev.holdbetter.common.GameResult
import dev.holdbetter.common.GameResult.*
import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.common.TeamWithMatchesDTO
import dev.holdbetter.core_network.model.RemoteLivescoreConfig.ALTER_IMAGE_HOST
import dev.holdbetter.core_network.model.RemoteLivescoreConfig.IMAGE_HOST
import dev.holdbetter.database.entity.DayLimitEntity
import dev.holdbetter.database.entity.Match
import dev.holdbetter.database.entity.Team
import dev.holdbetter.database.table.MonthLimits
import dev.holdbetter.innerApi.model.DayLimit
import dev.holdbetter.innerApi.model.MonthLimit
import dev.holdbetter.presenter.LimitsResolver
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import org.jetbrains.exposed.sql.ResultRow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

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
            image = imageResolver(team.alterImageId, team.image),
            gamePlayed = team.gamePlayed,
            points = team.points,
            wins = team.wins,
            loses = team.loses,
            draws = team.draws,
            goalsFor = team.goalsFor,
            goalsAgainst = team.goalsAgainst,
            goalsDiff = team.goalsDiff,
            alterImageId = team.alterImageId,
            twitter = team.twitter
        )
    }

    fun toStandings(
        team: Team,
        lastFiveMatches: List<MatchdayDTO>,
        liveMatch: MatchdayDTO?
    ): TeamRankDTO {
        return with(team) {
            val id = teamId.value
            TeamRankDTO(
                id = id,
                rank = rank,
                name = name,
                image = imageResolver(alterImageId, image),
                gamePlayed = gamePlayed,
                points = points,
                wins = wins,
                loses = loses,
                draws = draws,
                goalsFor = goalsFor,
                goalsAgainst = goalsAgainst,
                goalsDiff = goalsDiff,
                alterImageId = alterImageId,
                twitter = twitter,
                lastFiveResults = lastFiveMatches.map { it.toResult(id) },
                liveMatch = liveMatch
            )
        }
    }

    fun toModel(dayLimitEntity: DayLimitEntity): DayLimit {
        return with(dayLimitEntity) {
            DayLimit(
                gameDayDuration = gameDayDuration,
                plannedDayLimit = plannedDayLimit,
                firstMatchStartOrDefault = firstMatchStartOrDefault,
                remainedDayLimit = remainedDayLimit,
                updateRate = updateRate
            )
        }
    }

    fun toModel(
        dayLimitEntity: DayLimitEntity?,
        dateOnNullResult: LocalDate
    ): DayLimit {
        return if (dayLimitEntity != null) {
            with(dayLimitEntity) {
                DayLimit(
                    gameDayDuration = gameDayDuration,
                    plannedDayLimit = plannedDayLimit,
                    firstMatchStartOrDefault = firstMatchStartOrDefault,
                    remainedDayLimit = remainedDayLimit,
                    updateRate = updateRate
                )
            }
        } else {
            DayLimit(
                gameDayDuration = Duration.ZERO,
                plannedDayLimit = 0,
                firstMatchStartOrDefault = dateOnNullResult.atTime(
                    LimitsResolver.SAFE_DAY_TO_DAY_UPDATE_TIME_HOURS,
                    LimitsResolver.SAFE_DAY_TO_DAY_UPDATE_TIME_MINUTES
                ).toInstant(TimeZone.UTC),
                remainedDayLimit = 0,
                updateRate = LimitsResolver.SAFE_DAY_TO_DAY_UPDATE_RATE_IN_HOURS
                    .hours
                    .inWholeMinutes
                    .toInt()
            )
        }
    }

    fun ResultRow.toMonthLimit(): MonthLimit {
        return MonthLimit(
            month = this[MonthLimits.month],
            year = this[MonthLimits.year],
            plannedLimit = this[MonthLimits.plannedLimit],
            remainedLimit = this[MonthLimits.remainedLimit]
        )
    }

    fun Team.toTeamWithMatches(): TeamWithMatchesDTO {
        return TeamWithMatchesDTO(
            teamRank = TeamRankDTO(
                id = id.value,
                rank = rank,
                name = name,
                image = imageResolver(alterImageId, image),
                gamePlayed = gamePlayed,
                points = points,
                wins = wins,
                loses = loses,
                draws = draws,
                goalsFor = goalsFor,
                goalsAgainst = goalsAgainst,
                goalsDiff = goalsDiff,
                alterImageId = alterImageId,
                twitter = twitter
            ),
            teamMatches = constructTeamMatches()
        )
    }

    fun Team.constructTeamMatches() = homeMatches.map(::toModel) + awayMatches.map(::toModel)

    private fun MatchdayDTO.toResult(teamId: String): GameResult {
        val isHomeMatch = teamHomeId == teamId
        val home = resultHome.toLong()
        val away = resultAway.toLong()
        return when {
            home > away -> if (isHomeMatch) WIN else LOSE
            home < away -> if (isHomeMatch) LOSE else WIN
            else -> DRAW
        }
    }

    private fun imageResolver(alterImageId: String?, image: String) =
        alterImageId?.let { ALTER_IMAGE_HOST.replace("\${id}", it) }
            ?: "$IMAGE_HOST$image"
}