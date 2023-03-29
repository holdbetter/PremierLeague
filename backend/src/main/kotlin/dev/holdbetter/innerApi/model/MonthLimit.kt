package dev.holdbetter.innerApi.model

data class MonthLimit(
    val month: Int,
    val year: Int,
    val plannedLimit: Int,
    val remainedLimit: Int
)