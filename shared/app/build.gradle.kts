import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id(Plugins.androidMultiplatformLibrary)
    id(Plugins.composeMultiplatform)
    id(Plugins.composeCompiler)
    id(Plugins.metro)
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(21)
    androidLibrary {
        namespace = "dev.holdbetter.app"
        compileSdk = 36
        minSdk = 26
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "app"
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
        val commonMain by getting {
            dependencies {
//                implementation(project(":shared:core-navigation"))
                implementation(project(":shared:core-network"))
                implementation(project(":shared:core-di-api"))
                implementation(project(":shared:core-di-impl"))

                implementation(project(":shared:compose:design-system"))
                implementation(project(":shared:compose:feature-standings"))
            }
        }
        val androidMain by getting {
            dependencies {
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Deps.Common.kotlinCoroutines)
                implementation(Deps.Network.ktorClientDarwin)
            }
        }
        val iosTest by getting
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()
        .configureEach {
            compilations.configureEach {
                compilerOptions.configure {
                    freeCompilerArgs.add(
                        "-Xpartial-linkage-loglevel=INFO"
                    )
                }
            }
        }
}