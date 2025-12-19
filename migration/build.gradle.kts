plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("io.spring.dependency-management")
}

group = "net.skillgain"
version = "migration"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":persistence"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}