package dev.holdbetter.common

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class MatchdayDTO(
    val id: String,
    val resultHome: String,
    val resultAway: String,
    val teamHomeId: String,
    val teamAwayId: String,
    val status: String,
    val statusId: Int,
    val whoWon: Int,
    val startDate: Instant?,
    val endDate: Instant?,
    val teamHome: TeamRankDTO? = null,
    val teamAway: TeamRankDTO? = null
)