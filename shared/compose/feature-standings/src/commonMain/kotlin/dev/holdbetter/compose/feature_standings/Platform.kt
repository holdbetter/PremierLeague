package dev.holdbetter.compose.feature_standings

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform