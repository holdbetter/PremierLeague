package dev.holdbetter.premierleague

import kotlinx.serialization.Serializable

@Serializable
data class TeamDTO (
    val id: Long,
    val name: String,
    val logo: String
)