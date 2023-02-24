package dev.holdbetter.outerApi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class TeamRank(
    @SerialName(value = "Tid")
    val id: String,
    @SerialName(value = "rnk")
    val rank: Int,
    @SerialName(value = "Tnm")
    val name: String,
    @SerialName(value = "Img")
    val image: String,
    @SerialName(value = "pld")
    val gamePlayed: Int,
    @SerialName(value = "ptsn")
    val points: Int,
    @SerialName(value = "win")
    val win: Int,
    @SerialName(value = "lst")
    val loses: Int,
    @SerialName(value = "drw")
    val draws: Int,
    @SerialName(value = "gf")
    val goalsFor: Int,
    @SerialName(value = "ga")
    val goalsAgainst: Int,
    @SerialName(value = "gd")
    val goalsDiff: Int
)