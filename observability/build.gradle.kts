plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("io.spring.dependency-management")
}

group = "net.skillgain"
version = "observability"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":config"))
    implementation(project(":exception"))
    implementation(project(":common"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}