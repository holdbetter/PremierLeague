package dev.holdbetter.outerApi.model

import dev.holdbetter.outerApi.util.StandingsUnwrapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class LivescoreDataResponse(
    @SerialName(value = "Sid")
    val id: Long,
    @SerialName(value = "Snm")
    val name: String,
    @SerialName(value = "CompId")
    val competitionId: String,
    @SerialName(value = "CompD")
    val description: String,
    @SerialName(value = "Scd")
    val queryCode: String,
    @SerialName(value = "Ccd")
    val country: String,
    @SerialName(value = "Events")
    val matches: List<Matchday>,
    @SerialName(value = "LeagueTable")
    @Serializable(with = StandingsUnwrapper::class)
    val standings: List<TeamRank>,
)