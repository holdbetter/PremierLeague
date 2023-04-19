package dev.holdbetter.common

import kotlinx.serialization.Serializable

@Serializable
data class TeamWithMatchesDTO(
    val teamRank: TeamRankDTO,
    val teamMatches: List<MatchdayDTO>,
    val isCompareFeatureAvailable: Boolean = false
)