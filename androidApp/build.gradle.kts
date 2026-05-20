import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    kotlin("android")
    id(Plugins.metro)
    id(Plugins.composeMultiplatform)
    id(Plugins.composeCompiler)
    id(Plugins.googleServices).version(Versions.googleServicesVersion)
    id(Plugins.crashlytics).version(Versions.crashlyticsVersion)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

android {
    namespace = "dev.holdbetter.premierleague.android"
    compileSdk = 36
    defaultConfig {
        applicationId = "dev.holdbetter.premierleague.android"
        minSdk = 26
        targetSdk = 36
        versionCode = 2
        versionName = "0.1.1"

        buildConfigField("Boolean", "IS_COMPOSE", "false")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.activityKtx)
    implementation(Deps.AndroidX.splash)
    implementation(Deps.AndroidX.fragmentKtx)
    implementation(Deps.AndroidX.navigationKtx)
    implementation(Deps.AndroidX.navigationRuntimeKtx)
    implementation(Deps.AndroidX.navigationRuntimeKtx)
    implementation(Deps.Common.loggerNapier)

    // Meta Compose
    implementation(Deps.Compose.composeUiToolingPreview)
    debugImplementation(Deps.Compose.composeUiTooling)

    // Compose
    implementation(Deps.AndroidX.activityCompose)


    implementation(project.dependencies.platform(Deps.AndroidX.firebaseBom))
    implementation(Deps.AndroidX.firebaseAnalytics)
    implementation(Deps.AndroidX.firebaseCrashlytics)

    testImplementation(Deps.AndroidX.navigationTests)

    implementation(project(":shared:assets"))
    implementation(project(":shared:feature-standings-api"))
    implementation(project(":shared:feature-standings-impl"))
    implementation(project(":shared:feature-team-detail-api"))
    implementation(project(":shared:feature-team-detail-impl"))
    implementation(project(":shared:core-network"))
    implementation(project(":shared:core-navigation"))
    implementation(project(":shared:core-di-api"))
    implementation(project(":shared:core-di-impl"))
    implementation(project(":shared:core-database"))
    implementation(project(":shared:compose:feature-standings"))
}