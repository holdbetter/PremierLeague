package dev.holdbetter.outerApi.model

enum class StatusId(val id: Int) {
    NOT_STARTED(1),
    POSTPONED(5),
    FULL_TIME(6),
    HALF_TIME(10)
}
