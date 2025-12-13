plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.noarg")
    id("io.spring.dependency-management")
}

group = "net.skillgain"
version = "api"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":service"))
    implementation(project(":domain"))
    implementation(project(":config"))
    implementation(project(":exception"))
    implementation(project(":security"))
    implementation(project(":observability"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}