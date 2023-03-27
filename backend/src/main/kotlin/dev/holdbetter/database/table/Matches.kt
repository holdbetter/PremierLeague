package dev.holdbetter.database.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object Matches : IdTable<String>("Matches") {

    override val id = varchar("eventId", 12).entityId()

    val resultHome = varchar("resultHome", 2)
    val resultAway = varchar("resultAway", 2)
    val teamHome = reference("teamHomeId", Standings.id)
    val teamAway = reference("teamAwayId", Standings.id)
    val status = varchar("status", 10)
    val statusId = integer("statusId")
    val whoWon = integer("whoWon")
    val startDate = timestamp("startDate").nullable()
    val endDate = timestamp("endDate").nullable()

    override val primaryKey = PrimaryKey(id)
}