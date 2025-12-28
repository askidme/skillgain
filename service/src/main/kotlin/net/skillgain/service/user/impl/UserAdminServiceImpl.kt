package net.skillgain.service.user.impl

import net.skillgain.domain.entity.user.User
import net.skillgain.domain.entity.user.UserRole
import net.skillgain.domain.model.user.AuthProvider
import net.skillgain.domain.model.user.admin.CreateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRolesRequest
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

    override fun createUser(request: CreateUserRequest): User {


        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException(request.email)
        }

        val roleUser = roleRepository.findByName(UserRole.ROLE_USER.name)
            ?: throw RoleNotFoundException(UserRole.ROLE_USER.name)

        val user = User(
            email = request.email,
            password = null,
            firstName = request.firstName,
            lastName = request.lastName,
            active = request.active,
            authProvider = AuthProvider.LOCAL,
            forcePasswordChange = true
        )
            .also { it.addRole(roleUser) }

        return userRepository.save(user).also { inviteTokenService.sendInviteToken(it) }
    }

    override fun updateUser(userId: Long, request: UpdateUserRequest): User {
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException(userId) }
            .copy(
                firstName = request.firstName,
                lastName = request.lastName,
                phone = request.phone,
                active = request.active ?: false
            )

        return userRepository.save(user)
    }

    override fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

    override fun updateUserRoles(
        adminId: Long,
        targetUserId: Long,
        request: UpdateUserRolesRequest
    ): User {

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

        return userRepository.save(user)
    }

    override fun listUsers(): List<User> =
        userRepository.findAll()
}
