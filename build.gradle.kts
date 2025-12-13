import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.5.6" apply false
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false
    kotlin("plugin.jpa") version "1.9.25" apply false
    kotlin("plugin.noarg") version "1.9.25" apply false
}

allprojects {
    group = "net.skillgain"
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.6")
        }
    }
    dependencies {
        val archunitVersion = "1.2.1"
        val kotlinMockitoVersion = "5.2.1"
        val mockitoVersion = "5.2.0"

        "testImplementation"("com.tngtech.archunit:archunit:$archunitVersion")
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit5")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
        "testImplementation"("org.mockito.kotlin:mockito-kotlin:$kotlinMockitoVersion")
        "testImplementation"("org.mockito:mockito-core:$mockitoVersion")
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "21"
    }
}


