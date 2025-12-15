plugins {
    kotlin("multiplatform")
    id("com.android.library")
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
            baseName = "core-mvi"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Common.kotlinCoroutines)
                implementation(Deps.Common.loggerNapier)

                implementation(project(":shared:common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidUnitTest by getting
        val androidInstrumentedTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by getting
    }
}

android {
    namespace = "dev.holdbetter.coreMvi"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
}