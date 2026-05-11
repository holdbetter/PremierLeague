package dev.holdbetter.presenter.limits

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.time.Duration

data class DayInfo(
    val date: LocalDate,
    val count: Int,
    val duration: Duration,
    val firstMatchStart: Instant
)