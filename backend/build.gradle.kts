val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgresql_version: String by project
val kodein_version: String by project
//val compileKotlin: KotlinCompilationTask<*> by tasks

val localBuild = "isLocalBuild"

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.ktor.plugin").version(Versions.ktor_version)
}

group = "dev.holdbetter"
version = "0.0.1"

// usage only on Kotlin 1.8.0+
// compileKotlin.compilerOptions.freeCompilerArgs.add("-Xdebug")

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("dev.holdbetter.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm:${Versions.ktor_version}")
    implementation("io.ktor:ktor-server-core-jvm:${Versions.ktor_version}")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.ktor_version}")
    implementation("io.ktor:ktor-server-netty-jvm:${Versions.ktor_version}")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    implementation("org.postgresql:postgresql:$postgresql_version")

    implementation("io.ktor:ktor-client-core:${Versions.ktor_version}")
    implementation("io.ktor:ktor-client-okhttp:${Versions.ktor_version}")

    implementation("org.kodein.di:kodein-di:$kodein_version")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodein_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:${Versions.ktor_version}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation(project(":shared"))
}

tasks.register("buildLocalAndRun") {
    doLast {
        exec { commandLine("./gradlew", ":premierLeagueService:run", "--args=\"localBuild\"") }
    }
}

tasks.register("buildLocalAndDebug") {
    doLast {
        exec { commandLine("./gradlew", ":premierLeagueService:run", "--debug-jvm", "--args=\"localBuild\"") }
    }
}