package dev.holdbetter.core_network.util

const val IS_PRODUCTION = "IS_PRODUCTION"
const val PROD = "PROD"

internal val isDevelopment
    get() = System.getenv(IS_PRODUCTION) != PROD