package dev.holdbetter.presenter

import dev.holdbetter.common.Status.FULL_TIME
import dev.holdbetter.common.Status.POSTPONED
import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.common.TeamWithMatchesDTO
import dev.holdbetter.database.dao.*
import dev.holdbetter.database.query
import dev.holdbetter.database.table.DayLimits
import dev.holdbetter.database.table.Matches
import dev.holdbetter.database.table.MonthLimits
import dev.holdbetter.database.table.Standings
import dev.holdbetter.innerApi.model.DayLimit
import dev.holdbetter.interactor.DatabaseGateway
import dev.holdbetter.outerApi.Mapper.getMatches
import dev.holdbetter.outerApi.Mapper.getTeams
import dev.holdbetter.outerApi.model.LivescoreDataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils

// TODO: Database test
internal class DatabaseGatewayImpl(
    override val dispatcher: CoroutineDispatcher,
    private val database: Database
) : DatabaseGateway {

    private val teamDao: TeamDao = TeamDaoImpl(dispatcher, database)
    private val matchDao: MatchDao = MatchDaoImpl(dispatcher, database)
    private val dayLimitDao: DayLimitDao = DayLimitDaoImpl(dispatcher, database)
    private val monthLimitDao: MonthLimitDao = MonthLimitDaoImpl(dispatcher, database)

    override suspend fun getStandings(): List<TeamRankDTO> = teamDao.getTeams()

    override suspend fun getTeamWithMatches(teamId: String): TeamWithMatchesDTO? =
        teamDao.getTeamWithMatches(teamId)

    override suspend fun hasCache(): Boolean =
        database.query(dispatcher) {
            teamDao.hasData() && matchDao.hasData()
        }

    override suspend fun hasDayLimits() = dayLimitDao.hasData()

    override suspend fun hasMonthLimits() = monthLimitDao.hasData()

    override suspend fun createLeague(league: LivescoreDataResponse) {
        database.query(dispatcher) {
            SchemaUtils.create(Standings)
            SchemaUtils.create(Matches)
        }

        teamDao.insertTeams(league.getTeams())
        matchDao.insertMatches(league.getMatches())
    }

    override suspend fun fillDayLimits(dayLimitMap: Map<LocalDate, DayLimit>) {
        database.query(dispatcher) {
            SchemaUtils.create(DayLimits)
        }

        dayLimitDao.insertDayLimits(dayLimitMap)
    }

    override suspend fun initMonthLimits(monthToYears: Map<Int, Int>) {
        database.query(dispatcher) {
            SchemaUtils.create(MonthLimits)
        }

        monthLimitDao.fillLimits(monthToYears)
    }

    override suspend fun getRemainedMonthLimit(month: Int, year: Int): Int {
        return monthLimitDao.getRemainedLimit(month, year)
    }

    override suspend fun updateLimits(dayLimitMap: Map<LocalDate, DayLimit>) =
        dayLimitDao.updateDayLimits(dayLimitMap)

    override suspend fun updateLeague(league: LivescoreDataResponse) {
        val teams = league.getTeams()
        val matches = league.getMatches()

        teamDao.updateTeams(teams)
        matchDao.updateMatches(matches)
    }

    override suspend fun tryDecreaseRemainedLimit(date: LocalDate): Boolean = try {
        monthLimitDao.decreaseLimit(date.monthNumber, date.year)
        dayLimitDao.decreaseLimitByDate(date)
        true
    } catch (exposedSqlException: ExposedSQLException) {
        false
    }

    override suspend fun getNotStartedSortedMatches() =
        matchDao.getSortedMatches().filter {
            it.statusId != FULL_TIME.id && it.statusId != POSTPONED.id
                    && it.startDate != null
        }

    override suspend fun getDayLimitsForDate(date: LocalDate) = dayLimitDao.getLimitsForDate(date)
}