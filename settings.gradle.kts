pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PremierLeague"
include(":androidApp")
include(":backend")
include(":shared:util")
include(":shared:common")
include(":shared:core-mvi")
include(":shared:feature-standings-api")
include(":shared:core-network")
include(":shared:feature-standings-impl")
