package net.skillgain.security.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserPrincipal(
    val userId: Long,
    private val email: String,
    private val password: String?,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getUsername(): String = email
    override fun getPassword(): String? = password
    override fun getAuthorities() = authorities

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}
