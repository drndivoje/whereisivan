plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "rocks.drnd.whereisivan.testClient"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("javax.xml.parsers:jaxp-api:1.4.5")
    implementation("io.ktor:ktor-client-core:3.1.0")
    implementation("io.ktor:ktor-client-cio:3.1.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
tasks.jar {
    manifest {
        attributes["Main-Class"] = "rocks.drnd.whereisivan.testClient.MainKt"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
