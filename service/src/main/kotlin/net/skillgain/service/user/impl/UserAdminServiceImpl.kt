package net.skillgain.service.user.impl

import net.skillgain.domain.entity.user.UserRole
import net.skillgain.domain.mapper.user.toResponse
import net.skillgain.domain.mapper.user.toUser
import net.skillgain.domain.model.user.AuthProvider
import net.skillgain.domain.model.user.admin.*
import net.skillgain.exception.domain.user.UserException
import net.skillgain.exception.domain.user.UserRoleException
import net.skillgain.exception.domain.user.code.UserExceptionCode
import net.skillgain.exception.domain.user.code.UserRoleExceptionCode
import net.skillgain.persistence.repository.user.RoleRepository
import net.skillgain.persistence.repository.user.UserRepository
import net.skillgain.service.user.UserAdminService
import net.skillgain.service.user.UserInviteTokenService
import net.skillgain.service.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAdminServiceImpl(
    private val userService: UserService,
    private val roleRepository: RoleRepository,
    private val inviteTokenService: UserInviteTokenService,
    private val userRepository: UserRepository

) : UserAdminService {

    @Transactional
    override fun createUser(request: CreateUserRequest): UserResponse {

        if (userService.existsByEmail(request.email)) {
            throw UserException(UserExceptionCode.EMAIL_ALREADY_EXISTS, arrayOf(request.email))
        }

        val roleUser = roleRepository.findByName(UserRole.ROLE_USER.name)
            ?: throw UserRoleException(UserRoleExceptionCode.ROLE_NOT_FOUND, arrayOf(UserRole.ROLE_USER.name))

        val user = request.toUser(AuthProvider.LOCAL, mutableSetOf(roleUser))

        return userService.save(user).also { inviteTokenService.sendInviteToken(it) }.toResponse()
    }

    @Transactional
    override fun updateUser(userId: Long, request: UpdateUserRequest): UserResponse {
        val user = userService.findById(userId)

        return userService.save(request.toUser(user)).toResponse()
    }

    @Transactional
    override fun deleteUser(adminUserId: Long, userId: Long) = userService.delete(adminUserId, userId)

    @Transactional
    override fun updateUserRoles(adminId: Long, targetUserId: Long, request: UpdateUserRolesRequest): UserResponse {

        if (request.roles.isEmpty()) {
            throw UserRoleException(UserRoleExceptionCode.EMPTY_USER_ROLES)
        }

        if (adminId == targetUserId) {
            throw UserRoleException(UserRoleExceptionCode.ROLE_MODIFICATION_NOT_ALLOWED)
        }

        val user = userService.findById(targetUserId)

        val roles = roleRepository.findAllByNameIn(request.roles)

        if (roles.size != request.roles.size) {
            throw UserRoleException(UserRoleExceptionCode.INVALID_USER_ROLES)
        }

        user.roles.clear()
        user.roles.addAll(roles)

        return userService.save(user).toResponse()
    }

    override fun listUsers(): List<UserResponse> = userService.findAll().map { it.toResponse() }

    @Transactional
    override fun restoreUser(userId: Long): UserResponse {
        val user = userService.findByIdIncludingDeleted(userId)

        if (!user.isDeleted()) {
            return user.toResponse()
        }

        user.deletedAt = null

        return user.toResponse()
    }

    @Transactional
    override fun updateActivation(userId: Long, request: UpdateUserActivationRequest): UserResponse {
        val user = userService.findById(userId)

        user.active = request.active

        userService.save(user)
        return user.toResponse()
    }
}
