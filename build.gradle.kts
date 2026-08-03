plugins {
    //trick: for the same plugin versions in all sub-modules
    id(Plugins.androidApplication).version(Versions.androidGradlePlugin).apply(false)
    id(Plugins.androidMultiplatformLibrary).version(Versions.androidGradlePlugin).apply(false)
    kotlin("android").version(Versions.kotlinVersion).apply(false)
    kotlin("multiplatform").version(Versions.kotlinVersion).apply(false)
    id(Plugins.serialization).version(Versions.kotlinVersion).apply(false)
    id(Plugins.ksp).version(Versions.kspVersion).apply(false)
    id(Plugins.kotlinJvm).version(Versions.kotlinVersion).apply(false)
    id(Plugins.composeMultiplatform).version(Versions.composeMultiplatform).apply(false)
    id(Plugins.composeCompiler).version(Versions.kotlinVersion).apply(false)
    id(Plugins.metro).version(Versions.metroVersion).apply(false)
    id(Plugins.room3).version(Versions.room3Version).apply(false)
}

subprojects {
    configurations.all {
        resolutionStrategy {
            force("androidx.core:core:${Versions.coreKtxVersion}")
            force(Deps.AndroidX.appcompat)
            force(Deps.Common.kotlinSerialization)
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