import com.android.build.api.dsl.androidLibrary

plugins {
    kotlin("multiplatform")
    id(Plugins.androidMultiplatformLibrary)
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(21)
    androidLibrary {
        namespace = "dev.holdbetter.core_di_impl"
        compileSdk = 36
        minSdk = 26
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core-di-impl"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
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
                implementation(Deps.AndroidX.appcompat)
                implementation(Deps.AndroidX.fragmentKtx)
            }
        }
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