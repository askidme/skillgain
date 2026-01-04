package net.skillgain.service.user

import net.skillgain.domain.model.user.admin.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserAdminService {
    fun createUser(request: CreateUserRequest): UserResponse

    fun updateUser(userId: Long, request: UpdateUserRequest): UserResponse

    fun deleteUser(adminUserId: Long, userId: Long)

    fun updateUserRoles( adminId: Long, targetUserId: Long, request: UpdateUserRolesRequest): UserResponse

    fun listUsers(pageable: Pageable): Page<UserResponse>

    fun restoreUser(userId: Long): UserResponse

    fun updateActivation(userId: Long,request: UpdateUserActivationRequest): UserResponse
}