package dev.holdbetter.common

enum class Status(val id: Int) {
    NOT_STARTED(1),
    FIRST_TIME(2),
    SECOND_TIME(3),
    POSTPONED(5),
    FULL_TIME(6),
    HALF_TIME(10)
}
