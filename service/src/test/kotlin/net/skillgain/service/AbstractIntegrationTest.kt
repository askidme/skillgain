package net.skillgain.service

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

@SpringBootTest
@Transactional
@ActiveProfiles("test")
abstract class AbstractIntegrationTest {

    companion object {
        @Container
        @JvmField
        @ServiceConnection
        val postgres: PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>("postgres:15-alpine")
    }
}