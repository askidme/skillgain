plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("io.spring.dependency-management")
}

group = "net.skillgain"
version = "bpmn"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":service"))
    implementation(project(":domain"))
    implementation(project(":config"))
    implementation(project(":exception"))
    implementation(project(":observability"))
    implementation(project(":common"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}