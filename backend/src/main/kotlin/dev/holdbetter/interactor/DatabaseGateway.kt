package dev.holdbetter.interactor

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.core_network.DataSource.Database
import dev.holdbetter.innerApi.model.Limit
import dev.holdbetter.outerApi.model.LivescoreDataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.datetime.LocalDate

internal interface DatabaseGateway : Database {

    val dispatcher: CoroutineDispatcher

    suspend fun getStandings(): List<TeamRankDTO>

    suspend fun createLeague(league: LivescoreDataResponse)
    suspend fun createLimits(dayLimitMap: Map<LocalDate, Limit>)

    suspend fun updateLeague(league: LivescoreDataResponse)
    suspend fun updateLimits(dayLimitMap: Map<LocalDate, Limit>)

    suspend fun hasCache(): Boolean
    suspend fun hasLimits(): Boolean

    suspend fun tryDecreaseRemainedLimit(date: LocalDate): Boolean

    suspend fun getNotStartedMatches(): List<MatchdayDTO>
    suspend fun getUsedLimitUntilDate(date: LocalDate): Int
    suspend fun getLimitsForDate(date: LocalDate): Limit
}