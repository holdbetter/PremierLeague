object Deps {
    object Backend {
        const val ktorContentNegotiation = "io.ktor:ktor-server-content-negotiation-jvm:${Versions.ktorVersion}"
        const val ktorServerCore = "io.ktor:ktor-server-core-jvm:${Versions.ktorVersion}"
        const val ktorJson = "io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.ktorVersion}"
        const val ktorEngineNetty = "io.ktor:ktor-server-netty-jvm:${Versions.ktorVersion}"

        const val exposedCore = "org.jetbrains.exposed:exposed-core:${Versions.exposedVersion}"
        const val exposedDao = "org.jetbrains.exposed:exposed-dao:${Versions.exposedVersion}"
        const val exposedJdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposedVersion}"
        const val exposedTime = "org.jetbrains.exposed:exposed-kotlin-datetime:${Versions.exposedVersion}"

        const val kodein = "org.kodein.di:kodein-di:${Versions.kodeinVersion}"
        const val kodeinJvm = "org.kodein.di:kodein-di-framework-ktor-server-jvm:${Versions.kodeinVersion}"

        const val postgreSql = "org.postgresql:postgresql:${Versions.postgresqlVersion}"
    }

    object Architecture {
        const val mviKotlin = "com.arkivanov.mvikotlin:mvikotlin:${Versions.mviKotlinVersion}"
        const val mviKotlinCoroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:${Versions.mviKotlinVersion}"
    }

    object Network {
        const val ktorClient = "io.ktor:ktor-client-core:${Versions.ktorVersion}"
        const val ktorClientOkHttp = "io.ktor:ktor-client-okhttp:${Versions.ktorVersion}"
        const val ktorClientDarwin = "io.ktor:ktor-client-darwin:${Versions.ktorVersion}"
    }

    object Common {
        const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerializationVersion}"
        const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutinesVersion}"
        const val loggerNapier = "io.github.aakira:napier:${Versions.napierVersion}"
        const val kotlinTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinTimeVersion}"
    }

    object Js {
        const val browserWrapper = "org.jetbrains.kotlin-wrappers:kotlin-browser:${Versions.kotlinWrappers}"
    }

    object Compose {
        const val composeRuntime = "org.jetbrains.compose.runtime:runtime:${Versions.composeMultiplatform}"
        const val composeFoundation = "org.jetbrains.compose.foundation:foundation:${Versions.composeMultiplatform}"
        const val composeMaterial3 = "org.jetbrains.compose.material3:material3:${Versions.material3}"
        const val composeUi = "org.jetbrains.compose.ui:ui:${Versions.composeMultiplatform}"
        const val composeComponentResources = "org.jetbrains.compose.components:components-resources:${Versions.composeMultiplatform}"
        const val composeUiToolingPreview = "org.jetbrains.compose.ui:ui-tooling-preview:${Versions.composeMultiplatform}"
        const val composeUiTooling = "org.jetbrains.compose.ui:ui-tooling:${Versions.composeMultiplatform}"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
        const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtxVersion}"
        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtxVersion}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompatVersion}"
        const val splash = "androidx.core:core-splashscreen:${Versions.splashVersion}"
        const val palette = "androidx.palette:palette:${Versions.paletteVersion}"
        const val paletteKtx = "androidx.palette:palette-ktx:${Versions.paletteVersion}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
        const val recycler = "androidx.recyclerview:recyclerview:${Versions.recyclerVersion}"
        const val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
        const val pullToRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.pullToRefreshVersion}"
        const val navigationKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}"
        const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}"
        const val navigationRuntimeKtx = "androidx.navigation:navigation-runtime-ktx:${Versions.navigationVersion}"
        const val navigationFeatureModule = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigationVersion}"
        const val navigationTests = "androidx.navigation:navigation-testing:${Versions.navigationVersion}"

        const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseVersion}"
        const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"

        const val room = "androidx.room:room-runtime:${Versions.roomVersion}"
        const val roomKsp = "androidx.room:room-compiler:${Versions.roomVersion}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"

        const val viewModelCompose = "org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.androidxLifecycle}"
        const val runtimeCompose = "org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:${Versions.androidxLifecycle}"
    }

    object Test {
        const val ktorTest = "io.ktor:ktor-server-test-host:${Versions.ktorVersion}"
        const val junitParams = "org.junit.jupiter:junit-jupiter-params:${Versions.junitVersion}"
        const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junitVersion}"
        const val junitApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junitVersion}"
        const val kotlinJunit = "test-junit5"
    }
}

object Versions {
    const val kotlinVersion = "2.3.0"
    const val androidGradlePlugin = "8.13.2"
    const val composeMultiplatform = "1.11.0"
    const val material3 = "1.11.0-alpha07"

    const val ktorVersion = "3.0.3"
    const val exposedVersion = "0.56.0"
    const val postgresqlVersion = "42.7.4"
    const val kodeinVersion = "7.24.0"
    const val logbackVersion = "1.5.12"

    const val kotlinWrappers = "2026.5.3"
    const val kotlinSerializationVersion = "1.9.0"
    const val kotlinCoroutinesVersion = "1.9.0"
    const val kotlinTimeVersion = "0.6.0"
    const val napierVersion = "2.7.1"

    const val mviKotlinVersion = "4.0.0"
    const val glideVersion = "4.14.2"
    const val firebaseVersion = "33.7.0"

    const val coreKtxVersion = "1.18.0"
    const val activityKtxVersion = "1.9.3"
    const val activityCompose = "1.13.0"
    const val fragmentKtxVersion = "1.8.5"
    const val appcompatVersion = "1.7.0"
    const val splashVersion = "1.0.1"
    const val paletteVersion = "1.0.0"
    const val constraintLayoutVersion = "2.2.0"
    const val recyclerVersion = "1.3.2"
    const val pullToRefreshVersion = "1.1.0"
    const val navigationVersion = "2.8.5"
    const val roomVersion = "2.8.2"
    const val androidxLifecycle = "2.11.0-beta01"

    const val metroVersion = "1.1.1"
    const val googleServicesVersion = "4.3.15"
    const val kspVersion = "2.3.7"
    const val crashlyticsVersion = "3.0.2"

    // Test
    const val junitVersion = "5.10.2"
}

object Plugins {
    const val metro = "dev.zacsweers.metro"
    const val serialization = "org.jetbrains.kotlin.plugin.serialization"
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val androidMultiplatformLibrary = "com.android.kotlin.multiplatform.library"
    const val kotlinJvm = "org.jetbrains.kotlin.jvm"
    const val composeMultiplatform = "org.jetbrains.compose"
    const val composeCompiler = "org.jetbrains.kotlin.plugin.compose"
    const val ksp = "com.google.devtools.ksp"
    const val googleServices = "com.google.gms.google-services"
    const val crashlytics = "com.google.firebase.crashlytics"
}