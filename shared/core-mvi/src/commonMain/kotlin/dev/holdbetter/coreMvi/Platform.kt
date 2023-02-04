package dev.holdbetter.coreMvi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform