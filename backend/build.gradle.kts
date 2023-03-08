val kotlin_version: String by project
//val compileKotlin: KotlinCompilationTask<*> by tasks

val localBuild = "isLocalBuild"

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.ktor.plugin").version(Versions.ktorVersion)
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
    implementation(Deps.Backend.ktorServerCore)
    implementation(Deps.Backend.ktorJson)
    implementation(Deps.Backend.ktorContentNegotiation)
    implementation(Deps.Backend.ktorEngineNetty)

    implementation(Deps.Backend.exposedCore)
    implementation(Deps.Backend.exposedDao)
    implementation(Deps.Backend.exposedJdbc)
    implementation(Deps.Backend.exposedTime)

    implementation(Deps.Backend.postgreSql)

    implementation(Deps.Backend.kodein)
    implementation(Deps.Backend.kodeinJvm)

    implementation(Deps.Backend.logback)
    testImplementation(Deps.Backend.ktorTest)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation(project(":shared:common"))
    implementation(project(":shared:core-network"))
    implementation(project(":shared:core-di-api"))
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