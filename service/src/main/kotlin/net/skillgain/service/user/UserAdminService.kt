package net.skillgain.service.user

import net.skillgain.domain.entity.user.User
import net.skillgain.domain.model.user.admin.CreateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRolesRequest

interface UserAdminService {
    fun createUser(request: CreateUserRequest): User

    fun updateUser(userId: Long, request: UpdateUserRequest): User

    fun deleteUser(userId: Long)

    fun updateUserRoles( adminId: Long, targetUserId: Long, request: UpdateUserRolesRequest): User

    fun listUsers(): List<User>
}