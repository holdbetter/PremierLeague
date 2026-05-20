pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "PremierLeague"
include(":androidApp")
include(":backend")
include(":shared:common")
include(":shared:core-mvi")
include(":shared:core-network")
include(":shared:core-di-api")
include(":shared:core-di-impl")
include(":shared:feature-standings-api")
include(":shared:feature-standings-impl")
include(":shared:assets")
include(":shared:feature-team-detail-api")
include(":shared:feature-team-detail-impl")
include(":shared:feature-team-detail-example")
include(":shared:core-navigation")
include(":shared:core-database")
include(":shared:compose:feature-standings")
