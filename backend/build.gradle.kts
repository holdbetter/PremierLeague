import io.ktor.plugin.features.DockerImageRegistry

val kotlin_version: String by project

val localBuild = "isLocalBuild"

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.ktor.plugin").version(Versions.ktorVersion)
}

group = "dev.holdbetter"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("dev.holdbetter.ApplicationKt")
}

jib {
    from {
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
        }
    }
    container {
        ports = listOf("8080")
        mainClass = "dev.holdbetter.ApplicationKt"
    }
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("backend")
        imageTag.set("dev")

        externalRegistry.set(
            GhcrRegistry(
                username = providers.environmentVariable("GHCR_USERNAME"),
                githubToken = providers.environmentVariable("GHCR_TOKEN"),
                imageName = providers.environmentVariable("GHCR_IMAGE"),
            )
        )
    }
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
        exec {
            workingDir = project.rootDir
            commandLine("./gradlew", ":backend:run")
        }
    }
}

tasks.register("buildLocalAndDebug") {
    doLast {
        exec {
            workingDir = project.rootDir
            commandLine("./gradlew", ":backend:run", "--debug-jvm")
        }
    }
}

private class GhcrRegistry(
    override val username: Provider<String>,
    githubToken: Provider<String>,
    imageName: Provider<String>,
) : DockerImageRegistry {
    override val password: Provider<String> = githubToken
    override val toImage: Provider<String> = imageName
}