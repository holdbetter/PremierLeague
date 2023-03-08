package dev.holdbetter.database.entity

import dev.holdbetter.database.table.Matches
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

internal class Match(id: EntityID<String>) : Entity<String>(id) {

    companion object : EntityClass<String, Match>(Matches)

    val matchId by Matches.id
    val resultHome by Matches.resultHome
    val resultAway by Matches.resultAway
    val teamHomeId by Matches.teamHome
    val teamAwayId by Matches.teamAway
    val status by Matches.status
    val statusId by Matches.statusId
    val whoWon by Matches.whoWon
    val startDate by Matches.startDate
    val endDate by Matches.endDate

    val teamHome by Team referencedOn Matches.teamHome
    val teamAway by Team referencedOn Matches.teamAway
}