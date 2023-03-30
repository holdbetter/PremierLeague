plugins {
    id("com.android.application")
    kotlin("android")
    id(Plugins.dikt)
}

android {
    namespace = "dev.holdbetter.premierleague.android"
    compileSdk = 32
    defaultConfig {
        applicationId = "dev.holdbetter.premierleague.android"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    packagingOptions {
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
    implementation(Deps.AndroidX.navigationKtx)
    implementation(Deps.AndroidX.navigationRuntimeKtx)
    implementation(Deps.Common.loggerNapier)

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
}