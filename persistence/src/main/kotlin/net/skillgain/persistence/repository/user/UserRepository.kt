package net.skillgain.persistence.repository.user

import net.skillgain.domain.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmailAndDeletedAtIsNull(email: String): User?

    @Query("select u from User u left join fetch u.roles where u.email = :email")
    fun findByEmailWithRoles(email: String): User?

    fun existsByEmailAndDeletedAtIsNull(email: String): Boolean

    fun findByIdAndDeletedAtIsNull(id: Long): User?

}