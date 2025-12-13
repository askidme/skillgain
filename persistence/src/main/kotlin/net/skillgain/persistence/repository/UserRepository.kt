package net.skillgain.persistence.repository

import net.skillgain.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}