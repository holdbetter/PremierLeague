plugins {
    //trick: for the same plugin versions in all sub-modules
    id(Plugins.androidApplication).version(Versions.androidGradlePlugin).apply(false)
    id(Plugins.androidLibrary).version(Versions.androidGradlePlugin).apply(false)
    kotlin("android").version(Versions.kotlinVersion).apply(false)
    kotlin("multiplatform").version(Versions.kotlinVersion).apply(false)
    id(Plugins.serialization).version(Versions.kotlinVersion).apply(false)
    id(Plugins.dikt).version(Versions.diktVersion).apply(false)
    id(Plugins.ksp).version(Versions.kspVersion).apply(false)
    id(Plugins.kotlinJvm).version(Versions.kotlinVersion).apply(false)
    id(Plugins.composeMultiplatform).version(Versions.composeMultiplatform).apply(false)
    id(Plugins.composeCompiler).version(Versions.kotlinVersion).apply(false)
    id(Plugins.metro).version(Versions.metroVersion).apply(false)
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

buildscript {
    configurations.all {
        resolutionStrategy {
            force("org.apache.commons:commons-compress:1.26.0")
        }
    }
}