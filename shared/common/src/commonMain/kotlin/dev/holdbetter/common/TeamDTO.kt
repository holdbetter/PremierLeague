package dev.holdbetter.common

import kotlinx.serialization.Serializable

@Serializable
data class TeamDTO (
    val id: Long,
    val name: String,
    val logo: String
)