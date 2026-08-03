import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id(Plugins.androidMultiplatformLibrary)
    id(Plugins.metro)
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(21)
    androidLibrary {
        namespace = "dev.holdbetter.core_network"
        compileSdk = 36
        minSdk = 26
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core-network"
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
        val commonMain by getting {
            dependencies {
                implementation(Deps.Network.ktorClient)

                implementation(Deps.Common.kotlinSerialization)

                implementation(project(":shared:common"))
                implementation(project(":shared:core-di-api"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.Network.ktorClientOkHttp)
            }
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependencies {
                implementation(Deps.Network.ktorClientDarwin)
            }
        }
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(Deps.Network.ktorClientOkHttp)

                implementation(Deps.Backend.exposedCore)
                implementation(Deps.Backend.exposedDao)
                implementation(Deps.Backend.exposedJdbc)
            }
        }
    }
}
