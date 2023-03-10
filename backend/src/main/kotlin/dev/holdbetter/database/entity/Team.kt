package dev.holdbetter.database.entity

import dev.holdbetter.database.table.Matches
import dev.holdbetter.database.table.Standings
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class Team(id: EntityID<String>) : Entity<String>(id) {

    companion object : EntityClass<String, Team>(Standings)

    val teamId by Standings.id
    val rank by Standings.rank
    val name by Standings.name
    val image by Standings.image
    val gamePlayed by Standings.gamePlayed
    val points by Standings.points
    val wins by Standings.wins
    val loses by Standings.loses
    val draws by Standings.draws
    val goalsFor by Standings.goalsFor
    val goalsAgainst by Standings.goalsAgainst
    val goalsDiff by Standings.goalsDiff
    val alterImage by Standings.alterImage

    val homeMatches by Match referrersOn Matches.teamHome
    val awayMatches by Match referrersOn Matches.teamAway
}