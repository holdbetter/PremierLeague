package dev.holdbetter.database.entity

import dev.holdbetter.database.table.DayLimits
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class DayLimit(id: EntityID<LocalDate>) : Entity<LocalDate>(id) {

    companion object : EntityClass<LocalDate, DayLimit>(DayLimits)

    val date by DayLimits.id
    var gameDayDuration by DayLimits.gameDayDuration
    var firstMatchStartOrDefault by DayLimits.firstMatchStartOrDefault
    var plannedDayLimit by DayLimits.plannedDayLimit
    var remainedDayLimit by DayLimits.remainedDayLimit
    var updateRate by DayLimits.updateRate
}