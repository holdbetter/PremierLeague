package dev.holdbetter.presenter.limits

data class DayRate(
    val day: Int,
    val plannedRequestCount: Int,
    val info: DayInfo
) {
    val rateInSeconds: Int
        get() = (info.duration.inWholeSeconds / plannedRequestCount).toInt()
}