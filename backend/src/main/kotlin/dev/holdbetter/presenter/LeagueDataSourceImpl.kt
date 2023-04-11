package dev.holdbetter.presenter

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.core_network.DataSource.Database
import dev.holdbetter.core_network.DataSource.Network
import dev.holdbetter.core_network.model.Country
import dev.holdbetter.core_network.model.League
import dev.holdbetter.innerApi.model.DayLimit
import dev.holdbetter.interactor.DatabaseGateway
import dev.holdbetter.interactor.LeagueDataSource
import dev.holdbetter.interactor.NetworkGateway
import dev.holdbetter.isLeapYear
import io.ktor.utils.io.*
import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal typealias MonthAndYear = Pair<Int, Int>
internal typealias SortedMatchesGroupedByDayAndSeasonalMonth = Map<MonthAndYear, Map<Int, List<MatchdayDTO>>>

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
        makeLimits(
            notStartedMatches = database.getNotStartedSortedMatches(),
            databaseGateway = database,
            isLeapYear = today.year.isLeapYear
        )

        val nextDay = today.date.plus(1, DateTimeUnit.DAY)
        return calculateDelay(
            today = today,
            todayLimit = database.getDayLimitsForDate(today.date),
            nextDayLimit = database.getDayLimitsForDate(nextDay)
        )
    }
    override fun calculateDelay(
        today: LocalDateTime,
        todayLimit: DayLimit,
        nextDayLimit: DayLimit
    ): Duration {
        return if (todayLimit.remainedDayLimit > 0) {
            if (todayLimit.gameDayDuration != Duration.ZERO) {
                todayLimit.updateRate.minutes
            } else {
                nextDayLimit.firstMatchStartOrDefault
                    .minus(today.toInstant(TimeZone.UTC))
            }
        } else {
            nextDayLimit.firstMatchStartOrDefault
                .minus(today.toInstant(TimeZone.UTC))
        }
    }


    // TODO: Network exception handling / Retry policy / Analytics
    private suspend fun createOrUpdateCache(
        leagueParameter: League,
        countryParameter: Country
    ) {
        try {
            val league = network.getLeague(leagueParameter, countryParameter)
            if (!database.hasCache()) {
                database.createLeague(league)
            } else {
                database.updateLeague(league)
            }
        } catch (e: Exception) {
            println("On league createOrUpdate exception")
            e.printStack()
        }
    }

    private suspend fun makeLimits(
        notStartedMatches: List<MatchdayDTO>,
        databaseGateway: DatabaseGateway,
        isLeapYear: Boolean
    ) {
        if (!databaseGateway.hasMonthLimits()) {
            notStartedMatches.run(::getMonthAndYear)
                .run(::addCurrentMonthIfNotPresent)
                .also { databaseGateway.initMonthLimits(it) }
        }

        if (!databaseGateway.hasDayLimits()) {
            notStartedMatches.run(::groupMatchesByMonthAndByDay)
                .run {
                    LimitsResolver.generateDayLimits(
                        groupedMatches = this,
                        databaseGateway = databaseGateway,
                        leapYear = isLeapYear
                    )
                }
                .also { databaseGateway.fillDayLimits(it) }
        } else {
            notStartedMatches.run { groupMatchesByMonthAndByDay(this, true) }
                .run {
                    LimitsResolver.generateDayLimits(
                        groupedMatches = this,
                        databaseGateway = databaseGateway,
                        leapYear = isLeapYear
                    )
                }
                .also { databaseGateway.updateLimits(it) }
        }
    }

    private fun getMonthAndYear(matches: List<MatchdayDTO>): MutableMap<Int, Int> {
        return matches.associate {
            val startLocalDate = it.startDate!!.toLocalDateTime(TimeZone.UTC).date
            startLocalDate.monthNumber to startLocalDate.year
        }.toMutableMap()
    }

    private fun addCurrentMonthIfNotPresent(monthAndYear: MutableMap<Int, Int>): MutableMap<Int, Int> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        if (!monthAndYear.containsKey(today.monthNumber)) {
            monthAndYear[today.monthNumber] = today.year
        }
        return monthAndYear
    }

    private fun groupMatchesByMonthAndByDay(
        matches: List<MatchdayDTO>,
        excludeUntilToday: Boolean = false
    ): SortedMatchesGroupedByDayAndSeasonalMonth {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        return matches.filter { match -> match.startDate != null }
            .dropWhile {
                val startLocalDate = it.startDate?.toLocalDateTime(TimeZone.UTC)?.date
                excludeUntilToday && startLocalDate == today
            }
            .groupBy { match ->
                val date = match.startDate!!.toLocalDateTime(TimeZone.UTC).date
                date.monthNumber to date.year
            }
            .mapValues { monthAndMatches ->
                monthAndMatches.value.groupBy { match ->
                    match.startDate!!.toLocalDateTime(TimeZone.UTC).dayOfMonth
                }.toList()
                    .sortedBy { dayMatches -> dayMatches.second.count() }
                    .toMap()
            }
    }
}