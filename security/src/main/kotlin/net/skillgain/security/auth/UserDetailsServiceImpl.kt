package net.skillgain.security.auth

import net.skillgain.persistence.repository.user.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {

        val user = userRepository.findByEmailWithRoles(email)
            ?: throw UsernameNotFoundException("User not found")

        val authorities = user.roles
            .map { role -> SimpleGrantedAuthority(role.name) }

        return CustomUserPrincipal(
            userId = user.id,
            email = user.email,
            password = user.password,
            authorities = user.roles.map {
                SimpleGrantedAuthority(it.name)
            }
        )
    }
}