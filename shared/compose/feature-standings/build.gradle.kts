import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id(Plugins.androidLibrary)
    id(Plugins.composeMultiplatform)
    id(Plugins.composeCompiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
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

    android {
        namespace = "dev.holdbetter.compose.feature_standings"
        compileSdk = 36

        defaultConfig {
            minSdk = 26
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }
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