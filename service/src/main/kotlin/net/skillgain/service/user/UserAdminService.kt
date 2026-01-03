package net.skillgain.service.user

import net.skillgain.domain.model.user.admin.CreateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRolesRequest
import net.skillgain.domain.model.user.admin.UserResponse

interface UserAdminService {
    fun createUser(request: CreateUserRequest): UserResponse

    fun updateUser(userId: Long, request: UpdateUserRequest): UserResponse

    fun deleteUser(adminUserId: Long, userId: Long)

    fun updateUserRoles( adminId: Long, targetUserId: Long, request: UpdateUserRolesRequest): UserResponse

    fun listUsers(): List<UserResponse>
}