plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.9.1").apply(false)
    id("com.android.library").version("8.9.1").apply(false)
    kotlin("android").version("2.1.0").apply(false)
    kotlin("multiplatform").version("2.1.0").apply(false)
    id("org.jetbrains.kotlin.plugin.serialization").version("2.1.0").apply(false)
    id(Plugins.dikt).version(Versions.diktVersion).apply(false)
    id(Plugins.ksp).version(Versions.kspVersion).apply(false)
}

subprojects {
    configurations.all {
        resolutionStrategy {
            force("androidx.core:core:1.15.0")
            force("androidx.appcompat:appcompat:1.7.0")
            force("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
        }
    }
}