plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id(Plugins.dikt)
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "feature-team-detail-impl"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Common.kotlinCoroutines)
                implementation(Deps.Common.kotlinSerialization)
                implementation(Deps.Common.kotlinTime)
                implementation(Deps.Common.loggerNapier)

                implementation(project(":shared:core-mvi"))
                implementation(project(":shared:core-network"))
                implementation(project(":shared:core-di-api"))
                implementation(project(":shared:core-di-impl"))
                implementation(project(":shared:core-navigation"))
                implementation(project(":shared:core-database"))
                implementation(project(":shared:common"))
                implementation(project(":shared:feature-team-detail-api"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":shared:assets"))

                implementation(Deps.AndroidX.appcompat)
                implementation(Deps.AndroidX.constraintLayout)
                implementation(Deps.AndroidX.fragmentKtx)
                implementation(Deps.AndroidX.palette)
                implementation(Deps.AndroidX.recycler)
                implementation(Deps.AndroidX.glide)
                implementation(Deps.AndroidX.navigationKtx)
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "dev.holdbetter.shared.feature_team_detail_impl"
    compileSdk = 32
    defaultConfig {
        minSdk = 26
        targetSdk = 32
    }

    buildFeatures {
        viewBinding = true
    }
}