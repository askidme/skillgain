package net.skillgain.service.user.impl

import net.skillgain.domain.entity.user.UserRole
import net.skillgain.domain.mapper.user.toResponse
import net.skillgain.domain.mapper.user.toUser
import net.skillgain.domain.model.user.AuthProvider
import net.skillgain.domain.model.user.admin.CreateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRolesRequest
import net.skillgain.domain.model.user.admin.UserResponse
import net.skillgain.exception.domain.user.role.EmptyUserRolesException
import net.skillgain.exception.domain.user.role.InvalidUserRolesException
import net.skillgain.exception.domain.user.role.RoleModificationException
import net.skillgain.exception.domain.user.role.RoleNotFoundException
import net.skillgain.exception.domain.user.user.UserAlreadyExistsException
import net.skillgain.exception.domain.user.user.UserNotFoundException
import net.skillgain.persistence.repository.user.RoleRepository
import net.skillgain.persistence.repository.user.UserRepository
import net.skillgain.service.user.UserAdminService
import net.skillgain.service.user.UserInviteTokenService
import org.springframework.stereotype.Service

@Service
class UserAdminServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val inviteTokenService: UserInviteTokenService

) : UserAdminService {

    override fun createUser(request: CreateUserRequest): UserResponse {

        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException(request.email)
        }

        val roleUser = roleRepository.findByName(UserRole.ROLE_USER.name)
            ?: throw RoleNotFoundException(UserRole.ROLE_USER.name)

        val user = request.toUser(AuthProvider.LOCAL, mutableSetOf(roleUser))

        return userRepository.save(user).also { inviteTokenService.sendInviteToken(it) }.toResponse()
    }

    override fun updateUser(userId: Long, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException(userId) }

        return userRepository.save(request.toUser(user)).toResponse()
    }

    override fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

    override fun updateUserRoles(
        adminId: Long,
        targetUserId: Long,
        request: UpdateUserRolesRequest
    ): UserResponse {

        if(request.roles.isEmpty()) {
            throw EmptyUserRolesException()
        }

        if (adminId == targetUserId) {
            throw RoleModificationException()
        }

        val user = userRepository.findById(targetUserId).orElseThrow { UserNotFoundException(targetUserId) }

        val roles = roleRepository.findAllByNameIn(request.roles)

        if (roles.size != request.roles.size) {
            throw InvalidUserRolesException()
        }

        user.roles.clear()
        user.roles.addAll(roles)

        return userRepository.save(user).toResponse()
    }

    override fun listUsers(): List<UserResponse> =  userRepository.findAll().map { it.toResponse() }
}
