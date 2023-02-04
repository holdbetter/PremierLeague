package dev.holdbetter.common

import dev.holdbetter.common.util.StandingListUnwrapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeagueDTO (
    val id: Long,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String,
    val season: Int,
    @Serializable(with = StandingListUnwrapper::class)
    val standings: List<StandingDTO>
) {
    @Serializable
    data class StandingDTO (
        val rank: Int,
        val team: TeamDTO,
        val points: Int,
        val goalsDiff: Int,
        val group: String,
        val form: String,
        val description: String? = null,
        @SerialName("all")
        val stats: StatsDTO,
        @SerialName("home")
        val homeStats: StatsDTO,
        @SerialName("away")
        val awayStats: StatsDTO,
        val update: String
    )

    @Serializable
    data class StatsDTO (
        val played: Int,
        val win: Int,
        val draw: Int,
        val lose: Int,
        val goals: GoalsDTO
    )

    @Serializable
    data class GoalsDTO (
        @SerialName("for")
        val goalsFor: Long,
        val against: Long
    )
}