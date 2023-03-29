package dev.holdbetter.interactor

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.common.TeamWithMatchesDTO
import dev.holdbetter.core_network.DataSource.Database
import dev.holdbetter.innerApi.model.DayLimit
import dev.holdbetter.outerApi.model.LivescoreDataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.datetime.LocalDate

internal interface DatabaseGateway : Database {

    val dispatcher: CoroutineDispatcher

    suspend fun getStandings(): List<TeamRankDTO>
    suspend fun getTeamWithMatches(teamId: String): TeamWithMatchesDTO?

    suspend fun createLeague(league: LivescoreDataResponse)

    suspend fun initMonthLimits(monthToYears: Map<Int, Int>)
    suspend fun fillDayLimits(dayLimitMap: Map<LocalDate, DayLimit>)
    suspend fun updateLeague(league: LivescoreDataResponse)

    suspend fun updateLimits(dayLimitMap: Map<LocalDate, DayLimit>)
    suspend fun hasCache(): Boolean

    suspend fun hasDayLimits(): Boolean
    suspend fun hasMonthLimits(): Boolean
    suspend fun tryDecreaseRemainedLimit(date: LocalDate): Boolean

    suspend fun getNotStartedSortedMatches(): List<MatchdayDTO>
    suspend fun getDayLimitsForDate(date: LocalDate): DayLimit
    suspend fun getRemainedMonthLimit(month: Int, year: Int): Int
}