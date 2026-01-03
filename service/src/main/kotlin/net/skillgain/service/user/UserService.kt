package net.skillgain.service.user

import net.skillgain.domain.entity.user.User

interface UserService {

    fun findAll(): List<User>

    fun findById(userId: Long): User

    fun findByEmail(email: String): User

    fun getByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    fun save(user: User): User

    fun delete(adminUserId: Long,user: User)

    fun delete(adminUserId: Long,userId: Long)
}