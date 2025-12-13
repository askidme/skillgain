package net.skillgain.app

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchitectureTest {

    private val classes: JavaClasses = ClassFileImporter().importPackages("net.skillgain")

    @Test
    fun `config module should not depend on service module`() {
        noClasses()
            .that().resideInAPackage("..config..")
            .should().dependOnClassesThat().resideInAnyPackage("..service..")
            .allowEmptyShould(true)
            .check(classes)
    }

    @Test
    fun `cache module should not depend on service or ai`() {
        noClasses()
            .that().resideInAPackage("..cache..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..service..", "..ai..")
            .allowEmptyShould(true)
            .check(classes)
    }

    @Test
    fun `domain module should not depend on service or persistence`() {
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..service..", "..persistence..")
            .allowEmptyShould(true)
            .check(classes)
    }

    @Test
    fun `observe no circular dependencies in main codebase`() {
        com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices()
            .matching("net.skillgain.(*)..")
            .should().beFreeOfCycles()
            .check(classes)
    }
}