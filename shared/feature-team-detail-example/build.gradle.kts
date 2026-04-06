plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "dev.holdbetter.shared.feature_team_detail_example"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.holdbetter.shared.feature_team_detail_example"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.activityKtx)
    implementation(Deps.AndroidX.fragmentKtx)
    implementation(Deps.Common.loggerNapier)

    implementation(project(":shared:assets"))
    implementation(project(":shared:feature-team-detail-api"))
    implementation(project(":shared:feature-team-detail-impl"))
    implementation(project(":shared:core-network"))
    implementation(project(":shared:core-di-api"))
    implementation(project(":shared:core-di-impl"))
}