import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id(Plugins.androidMultiplatformLibrary)
    id(Plugins.metro)
    id(Plugins.ksp)
    id(Plugins.room3)
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(21)
    androidLibrary {
        namespace = "dev.holdbetter.shared.core_database"
        compileSdk = 36
        minSdk = 26
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core-database"
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
                implementation(Deps.Common.room3)

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
                implementation(Deps.Common.sqlite)
            }
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependencies {
                implementation(Deps.Common.sqlite)
            }
        }
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by getting
    }
}

dependencies {
    add("kspAndroid", Deps.Common.room3Compiler)
    add("kspIosSimulatorArm64", Deps.Common.room3Compiler)
    add("kspAndroid", Deps.Common.room3Compiler)
    add("kspIosArm64", Deps.Common.room3Compiler)
    add("kspJs", Deps.Common.room3Compiler)
    add("kspWasmJs", Deps.Common.room3Compiler)
}