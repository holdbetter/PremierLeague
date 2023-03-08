package dev.holdbetter.common

import kotlinx.serialization.Serializable

@Serializable
data class TeamRankDTO(
    val id: String,
    val rank: Int,
    val name: String,
    val image: String,
    val gamePlayed: Int,
    val points: Int,
    val wins: Int,
    val loses: Int,
    val draws: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalsDiff: Int
)