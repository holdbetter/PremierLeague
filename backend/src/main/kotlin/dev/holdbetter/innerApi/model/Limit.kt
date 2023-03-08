package dev.holdbetter.innerApi.model

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

data class Limit(
    val gameDayDuration: Duration,
    val plannedDayLimit: Int,
    val firstMatchStartOrDefault: LocalDateTime,
    val remainedDayLimit: Int,
    val updateRate: Int
)