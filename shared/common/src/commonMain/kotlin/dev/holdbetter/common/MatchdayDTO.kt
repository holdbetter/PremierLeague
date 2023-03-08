package dev.holdbetter.common

import kotlinx.datetime.LocalDateTime

data class MatchdayDTO(
    val id: String,
    val resultHome: String,
    val resultAway: String,
    val teamHomeId: String,
    val teamAwayId: String,
    val status: String,
    val statusId: Int,
    val whoWon: Int,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val teamHome: TeamRankDTO? = null,
    val teamAway: TeamRankDTO? = null
)
