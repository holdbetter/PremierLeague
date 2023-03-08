package dev.holdbetter.presenter

import dev.holdbetter.core_network.DataSource.Database
import dev.holdbetter.core_network.DataSource.Network
import dev.holdbetter.core_network.model.Country
import dev.holdbetter.core_network.model.League
import dev.holdbetter.innerApi.model.Limit
import dev.holdbetter.innerApi.model.Matchday
import dev.holdbetter.interactor.DatabaseGateway
import dev.holdbetter.interactor.LeagueDataSource
import dev.holdbetter.interactor.NetworkGateway
import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal typealias MonthAndDayGroupedMatchesDaySorted = Map<Int, Map<Int, List<Matchday>>>

// TODO: Test
internal class LeagueDataSourceImpl(
    override val network: NetworkGateway,
    override val database: DatabaseGateway
) : LeagueDataSource,
    Network by network,
    Database by database {

    override suspend fun updateLeagueData(
        leagueParameter: League,
        countryParameter: Country
    ): Duration {
        // getNewData
        createOrUpdateCache(leagueParameter, countryParameter)

        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val isLeapYear = today.year % 4 == 0
        makeLimits(
            notStartedMatches = database.getNotStartedMatches(),
            isLeapYear = isLeapYear,
            today = today
        )

        val nextDay = today.date.plus(1, DateTimeUnit.DAY)
        return calculateDelay(
            today = today,
            todayLimit = database.getLimitsForDate(today.date),
            nextDayLimit = database.getLimitsForDate(nextDay)
        )
    }
    override fun calculateDelay(
        today: LocalDateTime,
        todayLimit: Limit,
        nextDayLimit: Limit
    ): Duration {
        return if (todayLimit.remainedDayLimit > 0) {
            if (todayLimit.gameDayDuration != Duration.ZERO) {
                todayLimit.updateRate.minutes
            } else {
                nextDayLimit.firstMatchStartOrDefault.toInstant(TimeZone.UTC)
                    .minus(today.toInstant(TimeZone.UTC))
            }
        } else {
            nextDayLimit.firstMatchStartOrDefault.toInstant(TimeZone.UTC)
                .minus(today.toInstant(TimeZone.UTC))
        }
    }


    // TODO: Network exception handling / Retry policy / Analytics
    private suspend fun createOrUpdateCache(
        leagueParameter: League,
        countryParameter: Country
    ) {
        val league = network.getLeague(leagueParameter, countryParameter)
        if (!database.hasCache()) {
            database.createLeague(league)
        } else {
            database.updateLeague(league)
        }
    }

    private suspend fun makeLimits(
        notStartedMatches: List<Matchday>,
        isLeapYear: Boolean,
        today: LocalDateTime
    ) {
        if (!database.hasLimits()) {
            notStartedMatches.run(::groupMatchesByMonthAndByDay)
                .run { LimitsResolver.generateDayLimits(this, isLeapYear) }
                .also { database.createLimits(it) }
        } else {
            // TODO: Not tested
            val usedLimit = database.getUsedLimitUntilDate(today.date)
            notStartedMatches.run { groupMatchesByMonthAndByDay(this, true) }
                .run { LimitsResolver.generateDayLimits(this, isLeapYear, usedLimit) }
                .also { database.updateLimits(it) }
        }
    }

    private fun groupMatchesByMonthAndByDay(
        matches: List<Matchday>,
        excludeToday: Boolean = false
    ): MonthAndDayGroupedMatchesDaySorted {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        return matches.filter { match -> match.startDate != null }
            .dropWhile { excludeToday && it.startDate?.date == today }
            .groupBy { match -> match.startDate!!.monthNumber }
            .mapValues { monthAndMatches ->
                monthAndMatches.value.groupBy { match -> match.startDate!!.dayOfMonth }
                    .toList()
                    .sortedBy { dayMatches -> dayMatches.second.count() }
                    .toMap()
            }
    }
}