package dev.holdbetter.common

import kotlinx.serialization.Serializable

@Serializable
class TeamWithMatchesDTO(
    val teamRank: TeamRankDTO,
    val teamMatches: List<MatchdayDTO>
)