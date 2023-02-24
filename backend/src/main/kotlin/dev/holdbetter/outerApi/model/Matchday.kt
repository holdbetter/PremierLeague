package dev.holdbetter.outerApi.model

import dev.holdbetter.outerApi.util.TeamIdFromMatchUnwrapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class Matchday(
    @SerialName(value = "Eid")
    val id: String,
    @SerialName(value = "Tr1")
    val resultHome: String = "0",
    @SerialName(value = "Tr2")
    val resultAway: String = "0",
    @SerialName(value = "T1")
    @Serializable(with = TeamIdFromMatchUnwrapper::class)
    val teamHomeId: String,
    @SerialName(value = "T2")
    @Serializable(with = TeamIdFromMatchUnwrapper::class)
    val teamAwayId: String,
    @SerialName(value = "Eps")
    val status: String,
    @SerialName(value = "Esid")
    val statusId: Int,
    @SerialName(value = "Ewt")
    val whoWon: Int = -1,
    @SerialName(value = "Esd")
    val startDate: Long,
    @SerialName(value = "Edf")
    val endDate: Long = -1
)