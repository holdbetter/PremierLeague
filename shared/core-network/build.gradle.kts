plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id(Plugins.dikt)
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(21)
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core-network"
        }
    }

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Network.ktorClient)

                implementation(Deps.Common.dikt)
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
                implementation(Deps.Common.dikt)
            }
        }
        val androidUnitTest by getting
        val androidInstrumentedTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependencies {
                implementation(Deps.Network.ktorClientDarwin)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(Deps.Network.ktorClientOkHttp)

                implementation(Deps.Backend.exposedCore)
                implementation(Deps.Backend.exposedDao)
                implementation(Deps.Backend.exposedJdbc)

                implementation(Deps.Common.dikt)
            }
        }
    }
}

android {
    namespace = "dev.holdbetter.core_network"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
}