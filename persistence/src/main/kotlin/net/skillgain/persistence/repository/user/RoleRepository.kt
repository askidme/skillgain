package net.skillgain.persistence.repository.user

import net.skillgain.domain.entity.user.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: String): Role?
}