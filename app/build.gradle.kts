plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "net.skillgain"
version = "0.0.1-SNAPSHOT"
description = "app"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    implementation(project(":bpmn"))
    implementation(project(":service"))
    implementation(project(":ai"))
    implementation(project(":persistence"))
    implementation(project(":migration"))
    implementation(project(":domain"))
    implementation(project(":config"))
    implementation(project(":exception"))
    implementation(project(":security"))
    implementation(project(":observability"))
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
}


kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    jvmArgs = listOf(
        "-Dcom.sun.management.jmxremote=false",
        "-Dcom.sun.management.jmxremote.port=0",
        "-Dcom.sun.management.jmxremote.local.only=true"
    )
}