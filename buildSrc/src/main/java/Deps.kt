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
    }

    object Common {
        const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerializationVersion}"
    }
}

object Versions {

    const val ktorVersion = "2.2.2"
    const val exposedVersion = "0.41.1"
    const val postgresqlVersion = "42.5.1"
    const val kodeinVersion = "7.18.0"
    const val logbackVersion = "1.2.11"

    const val kotlinSerializationVersion = "1.4.1"

    const val mviKotlinVersion = "3.1.0"
}