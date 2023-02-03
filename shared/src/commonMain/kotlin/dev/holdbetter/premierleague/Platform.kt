package dev.holdbetter.premierleague

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform