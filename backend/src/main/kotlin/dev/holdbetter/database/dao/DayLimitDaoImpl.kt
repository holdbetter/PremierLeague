package dev.holdbetter.database.dao

import dev.holdbetter.database.Mapper.toModel
import dev.holdbetter.database.entity.DayLimitEntity
import dev.holdbetter.database.query
import dev.holdbetter.database.table.DayLimits
import dev.holdbetter.database.table.MonthLimits
import dev.holdbetter.innerApi.model.DayLimit
import dev.holdbetter.presenter.DayLimitMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.BaseBatchInsertStatement

internal class DayLimitDaoImpl(
    override val dispatcher: CoroutineDispatcher,
    private val database: Database
) : DayLimitDao {

    override suspend fun decreaseLimitByDate(date: LocalDate) {
        database.query(dispatcher) {
            DayLimitEntity.findById(date)?.let { limit ->
                limit.remainedDayLimit = limit.remainedDayLimit - 1
            }
        }
    }

    override suspend fun insertDayLimits(dayLimitMap: DayLimitMap) {
        database.query(dispatcher) {
            DayLimits.batchInsert(
                data = dayLimitMap.asIterable(),
                ignore = false,
                shouldReturnGeneratedValues = false
            ) {
                statementMapper(it.key, it.value)
            }
        }
    }

    override suspend fun updateDayLimits(dayLimitMap: DayLimitMap) {
        database.query(dispatcher) {
            dayLimitMap.forEach {
                DayLimitEntity.findById(it.key)?.let { limit ->
                    with(limit) {
                        this.gameDayDuration = limit.gameDayDuration
                        this.firstMatchStartOrDefault = limit.firstMatchStartOrDefault
                        this.remainedDayLimit = limit.remainedDayLimit
                        this.updateRate = limit.updateRate
                    }
                }
            }
        }
    }

    override suspend fun getLimits(): DayLimitMap =
        database.query(dispatcher) {
            DayLimitEntity.all().associate {
                it.date.value to toModel(it)
            }
        }

    override suspend fun getLimitsForDate(date: LocalDate): DayLimit =
        database.query(dispatcher) {
            toModel(DayLimitEntity.findById(date), date)
        }

    // TODO: Add check season-to-season
    override suspend fun hasData(): Boolean =
        database.query(dispatcher) {
            DayLimits.exists() && DayLimits.selectAll().empty().not()
        }

    private fun BaseBatchInsertStatement.statementMapper(
        date: LocalDate,
        limit: DayLimit
    ) {
        this[DayLimits.id] = date
        this[DayLimits.gameDayDuration] = limit.gameDayDuration
        this[DayLimits.firstMatchStartOrDefault] = limit.firstMatchStartOrDefault
        this[DayLimits.plannedDayLimit] = limit.plannedDayLimit
        this[DayLimits.remainedDayLimit] = limit.remainedDayLimit
        this[DayLimits.updateRate] = limit.updateRate
    }
}