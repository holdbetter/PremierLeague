import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id(Plugins.androidMultiplatformLibrary)
    id(Plugins.composeMultiplatform)
    id(Plugins.composeCompiler)
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

    jvm()

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
            implementation(Deps.Compose.composeUiToolingPreview)
        }
        commonMain.dependencies {
            implementation(Deps.Compose.composeRuntime)
            implementation(Deps.Compose.composeFoundation)
            implementation(Deps.Compose.composeMaterial3)
            implementation(Deps.Compose.composeUi)
            implementation(Deps.Compose.composeComponentResources)
            implementation(Deps.Compose.composeUiToolingPreview)
            implementation(Deps.AndroidX.viewModelCompose)
            implementation(Deps.AndroidX.runtimeCompose)
        }
        jsMain.dependencies {
            implementation(Deps.Js.browserWrapper)
        }
    }
}