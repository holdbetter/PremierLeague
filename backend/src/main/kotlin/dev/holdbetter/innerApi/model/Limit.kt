package dev.holdbetter.innerApi.model

import kotlinx.datetime.Instant
import kotlin.time.Duration

data class Limit(
    val gameDayDuration: Duration,
    val plannedDayLimit: Int,
    val firstMatchStartOrDefault: Instant,
    val remainedDayLimit: Int,
    val updateRate: Int
)