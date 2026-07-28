import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id(Plugins.androidMultiplatformLibrary)
    id(Plugins.composeMultiplatform)
    id(Plugins.composeCompiler)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "dev.holdbetter.compose.design_system"
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(21)
    androidLibrary {
        namespace = "dev.holdbetter.compose.design_system"
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
            baseName = "design-system"
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
        commonMain.dependencies {
            implementation(Deps.Compose.composeUi)
            implementation(Deps.Compose.composeFoundation)
            implementation(Deps.Compose.composeComponentResources)
            implementation(Deps.Compose.composeMaterial3)
        }
    }
}