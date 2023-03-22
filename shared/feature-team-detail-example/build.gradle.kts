plugins {
    id("com.android.application")
    kotlin("android")
    id(Plugins.dikt)
}

android {
    namespace = "dev.holdbetter.shared.feature_team_detail_example"
    compileSdk = 32

    defaultConfig {
        applicationId = "dev.holdbetter.shared.feature_team_detail_example"
        minSdk = 26
        targetSdk = 32
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.material)
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