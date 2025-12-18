package net.skillgain.persistence.repository.user

import net.skillgain.domain.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}