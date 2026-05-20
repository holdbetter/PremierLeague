package dev.holdbetter

const val IS_PRODUCTION = "IS_PRODUCTION"
const val PROD = "PROD"

val isDevelopment
    get() = System.getenv(IS_PRODUCTION) != PROD

val Int.isLeapYear
    get() = this % 4 == 0
