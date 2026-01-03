package net.skillgain.persistence.repository.user

import net.skillgain.domain.entity.user.User
import net.skillgain.domain.entity.user.UserPasswordHistory
import org.springframework.data.jpa.repository.JpaRepository

interface UserPasswordHistoryRepository : JpaRepository<UserPasswordHistory, Long> {

    fun findTop3ByUserOrderByCreatedAtDesc(user: User): List<UserPasswordHistory>
}
