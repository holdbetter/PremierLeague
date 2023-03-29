package dev.holdbetter.database.table

import org.jetbrains.exposed.sql.Table

object MonthLimits : Table("MonthLimit") {

    val month = integer("month")
    val year = integer("year")
    val plannedLimit = integer("plannedLimit")
    val remainedLimit = integer("remainedLimit").check { it greaterEq 0 }

    override val primaryKey: PrimaryKey = PrimaryKey(arrayOf(year, month))
}