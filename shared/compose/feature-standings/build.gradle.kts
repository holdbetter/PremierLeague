@file:OptIn(DelicateMetroGradleApi::class, RequiresIdeSupport::class)

import com.android.build.api.dsl.androidLibrary
import dev.zacsweers.metro.gradle.DelicateMetroGradleApi
import dev.zacsweers.metro.gradle.RequiresIdeSupport
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id(Plugins.androidMultiplatformLibrary)
    id(Plugins.composeMultiplatform)
    id(Plugins.composeCompiler)
    id(Plugins.metro)
}

metro {
    enableTopLevelFunctionInjection.set(true)
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(21)
    androidLibrary {
        namespace = "dev.holdbetter.compose.feature_standings"
        compileSdk = 36
        minSdk = 26

        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        androidMain.dependencies {
            implementation(Deps.Compose.composeUiToolingPreview)
            implementation(Deps.AndroidX.coil)
            implementation(Deps.AndroidX.composeTracing)
        }
        commonMain.dependencies {
            implementation(Deps.Common.kotlinCoroutines)
            implementation(Deps.Common.kotlinSerialization)
            implementation(Deps.Common.kotlinTime)
            implementation(Deps.Common.loggerNapier)

            implementation(Deps.Compose.composeRuntime)
            implementation(Deps.Compose.composeFoundation)
            implementation(Deps.Compose.composeMaterial3)
            implementation(Deps.Compose.composeUi)
            implementation(Deps.Compose.composeComponentResources)
            implementation(Deps.Compose.composeUiToolingPreview)
            implementation(Deps.Compose.coil)
            implementation(Deps.Compose.coilNetwork)

            implementation(Deps.AndroidX.viewModelCompose)
            implementation(Deps.AndroidX.runtimeCompose)

            implementation(project(":shared:compose:design-system"))
            implementation(project(":shared:core-mvi"))
            implementation(project(":shared:core-network"))
            implementation(project(":shared:core-navigation"))
            implementation(project(":shared:core-database"))
            implementation(project(":shared:core-di-api"))
            implementation(project(":shared:core-di-impl"))
            implementation(project(":shared:common"))

            implementation(project(":shared:feature-standings-api"))
            implementation(project(":shared:feature-standings-impl"))
        }
        jsMain.dependencies {
            implementation(Deps.Js.browserWrapper)
        }
    }
}