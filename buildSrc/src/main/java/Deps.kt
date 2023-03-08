object Deps {
    object Backend {
        const val ktorContentNegotiation = "io.ktor:ktor-server-content-negotiation-jvm:${Versions.ktorVersion}"
        const val ktorServerCore = "io.ktor:ktor-server-core-jvm:${Versions.ktorVersion}"
        const val ktorJson = "io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.ktorVersion}"
        const val ktorEngineNetty = "io.ktor:ktor-server-netty-jvm:${Versions.ktorVersion}"
        const val ktorTest = "io.ktor:ktor-server-tests-jvm:${Versions.ktorVersion}"

        const val exposedCore = "org.jetbrains.exposed:exposed-core:${Versions.exposedVersion}"
        const val exposedDao = "org.jetbrains.exposed:exposed-dao:${Versions.exposedVersion}"
        const val exposedJdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposedVersion}"
        const val exposedTime = "org.jetbrains.exposed:exposed-kotlin-datetime:${Versions.exposedVersion}"

        const val kodein = "org.kodein.di:kodein-di:${Versions.kodeinVersion}"
        const val kodeinJvm = "org.kodein.di:kodein-di-framework-ktor-server-jvm:${Versions.kodeinVersion}"

        const val postgreSql = "org.postgresql:postgresql:${Versions.postgresqlVersion}"

        const val logback = "ch.qos.logback:logback-classic:${Versions.logbackVersion}"
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

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
        const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtxVersion}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtxVersion}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompatVersion}"
        const val material = "com.google.android.material:material:${Versions.material}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
        const val recycler = "androidx.recyclerview:recyclerview:${Versions.recyclerVersion}"
        const val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
        const val pullToRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.pullToRefreshVersion}"
    }
}

object Versions {

    const val ktorVersion = "2.2.2"
    const val exposedVersion = "0.41.1"
    const val postgresqlVersion = "42.5.1"
    const val kodeinVersion = "7.18.0"
    const val logbackVersion = "1.2.11"

    const val kotlinSerializationVersion = "1.4.1"
    const val kotlinCoroutinesVersion = "1.6.4"
    const val kotlinTimeVersion = "0.4.0"
    const val napierVersion = "2.6.1"

    const val mviKotlinVersion = "3.1.0"

    const val coreKtxVersion = "1.8.0"
    const val activityKtxVersion = "1.5.1"
    const val fragmentKtxVersion = "1.5.5"
    const val appcompatVersion = "1.5.1"
    const val material = "1.8.0"
    const val constraintLayoutVersion = "2.1.4"
    const val recyclerVersion = "1.2.0"
    const val pullToRefreshVersion = "1.1.0"

    const val diktVersion = "1.0.2"
    const val glideVersion = "4.14.2"
}

object Plugins {
    const val dikt = "io.github.sergeshustoff.dikt"
}