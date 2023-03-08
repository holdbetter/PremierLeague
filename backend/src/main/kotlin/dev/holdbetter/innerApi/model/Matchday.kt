package dev.holdbetter.innerApi.model

import kotlinx.datetime.LocalDateTime

internal data class Matchday(
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
    val teamHome: TeamRank? = null,
    val teamAway: TeamRank? = null
)
