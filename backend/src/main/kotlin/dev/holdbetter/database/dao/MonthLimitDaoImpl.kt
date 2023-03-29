package dev.holdbetter.database.dao

import dev.holdbetter.database.Mapper.toMonthLimit
import dev.holdbetter.database.query
import dev.holdbetter.database.table.MonthLimits
import dev.holdbetter.isLeapYear
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.minus

// TODO: Test
internal class MonthLimitDaoImpl(
    override val dispatcher: CoroutineDispatcher,
    private val database: Database
) : MonthLimitDao {

    private companion object {
        const val MONTH_LIMIT = 500
    }

    override suspend fun getRemainedLimit(month: Int, year: Int): Int {
        return database.query(dispatcher) {
            MonthLimits.selectAll()
                .map { it.toMonthLimit() }
                .first { it.month == month && it.year == year }
                .remainedLimit
        }
    }

    override suspend fun fillLimits(monthToYears: Map<Int, Int>) {
        database.query(dispatcher) {
            val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            monthToYears.forEach { monthToYear ->
                val (month, year) = monthToYear
                MonthLimits.insert {
                    val plannedLimit = calculateAppropriateMonthLimit(today, month, year)
                    it[this.month] = month
                    it[this.year] = year
                    it[this.plannedLimit] = plannedLimit
                    it[this.remainedLimit] = plannedLimit
                }
            }
        }
    }

    override suspend fun hasData(): Boolean {
        return database.query(dispatcher) {
            MonthLimits.exists() && MonthLimits.selectAll().empty().not()
        }
    }

    override suspend fun decreaseLimit(month: Int, year: Int) {
        val monthEq = MonthLimits.month eq month
        val yearEq = MonthLimits.year eq year
        database.query(dispatcher) {
            MonthLimits.update({ monthEq and yearEq }) {
                it[remainedLimit] = remainedLimit - 1
            }
        }
    }

    private fun calculateAppropriateMonthLimit(today: LocalDate, month: Int, year: Int): Int {
        val monthLength = Month(month).length(year.isLeapYear)
        return if (today.monthNumber == month && today.year == year && today.dayOfMonth != 1) {
            val maxDailyLimit = MONTH_LIMIT / monthLength
            (1 + monthLength - today.dayOfMonth) * maxDailyLimit
        } else {
            MONTH_LIMIT
        }
    }
}