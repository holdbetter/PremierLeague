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
include(":shared:core-network")
include(":shared:core-di-api")
include(":shared:core-di-impl")
include(":shared:feature-standings-api")
include(":shared:feature-standings-impl")
