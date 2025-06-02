val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val swagger_codegen_version: String by project

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "2.3.8"
    kotlin("plugin.serialization") version "1.9.22"
}

group = "rocks.drnd"
version = "0.0.1"

application {
    mainClass.set("rocks.drnd.whereisivan.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.swagger.codegen.v3:swagger-codegen-generators:$swagger_codegen_version")
    implementation("io.insert-koin:koin-ktor:$koin_version") // Koin for Ktor
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version") // Koin Logger
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation(kotlin("stdlib"))
    implementation("io.jenetics:jpx:3.1.0")
    //Test Dependency
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    // testImplementation(kotlin("test"))


}
kotlin {
    jvmToolchain(21)
}
ktor {
    docker {
        localImageName.set("whereisivan-backend")
        imageTag.set("0.0.1-SNAPSHOT")
        jreVersion.set(JavaVersion.VERSION_21)
        portMappings.set(listOf(
            io.ktor.plugin.features.DockerPortMapping(
                80,
                8080,
                io.ktor.plugin.features.DockerPortMappingProtocol.TCP
            )
        ))

    }
}