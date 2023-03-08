package dev.holdbetter.database.table

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.duration

internal object DayLimits : IdTable<LocalDate>("DayLimit") {

    override val id: Column<EntityID<LocalDate>> = date("date").entityId()

    val gameDayDuration = duration("gameDayDuration")
    val firstMatchStartOrDefault = datetime("firstMatchStartOrDefault")
    val plannedDayLimit = integer("plannedDayLimit")
    val remainedDayLimit = integer("remainedDayLimit").check { it greaterEq 0 }
    val updateRate = integer("updateRate")

    override val primaryKey = PrimaryKey(id)
}