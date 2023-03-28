package dev.holdbetter.shared.feature_team_detail

data class Team(
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
    val goalsDiff: Int,
    val twitter: String?
)