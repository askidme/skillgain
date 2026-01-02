package net.skillgain.security.config

import net.skillgain.security.auth.CustomUserPrincipal
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class AuditorAwareConfig{

    @Bean
    fun auditorProvider(): AuditorAware<Long> = AuditorAware {
        val auth = SecurityContextHolder.getContext().authentication

        if (auth == null || !auth.isAuthenticated || auth.principal == "anonymousUser") {
            Optional.empty()
        } else {
            (auth.principal as? CustomUserPrincipal) ?.userId ?.let { Optional.of(it) } ?: Optional.empty()
        }
    }
}