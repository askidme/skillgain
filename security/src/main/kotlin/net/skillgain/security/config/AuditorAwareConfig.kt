package net.skillgain.security.config

import net.skillgain.domain.entity.user.User
import net.skillgain.persistence.repository.user.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class AuditorAwareConfig(
    private val userRepository: UserRepository
) {

    @Bean
    fun auditorProvider(): AuditorAware<User> = AuditorAware {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated) {
            return@AuditorAware Optional.empty()
        }

        val email = authentication.name
        userRepository.findByEmail(email)?.let { Optional.of(it) }
            ?: Optional.empty()
    }
}