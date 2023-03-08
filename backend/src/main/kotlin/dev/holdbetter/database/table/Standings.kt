package dev.holdbetter.database.table

import org.jetbrains.exposed.dao.id.IdTable

internal object Standings : IdTable<String>("Standings") {

    override val id = varchar("teamId", 12).entityId()

    val rank = integer("rank")
    val name = varchar("name", 30)
    val image = varchar("image", 30)
    val gamePlayed = integer("gamePlayed")
    val points = integer("points")
    val wins = integer("wins")
    val loses = integer("loses")
    val draws = integer("draws")
    val goalsFor = integer("goalsFor")
    val goalsAgainst = integer("goalsAgainst")
    val goalsDiff = integer("goalsDiff")

    override val primaryKey = PrimaryKey(id)
}