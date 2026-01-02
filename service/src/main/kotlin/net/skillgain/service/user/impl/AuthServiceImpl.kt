package net.skillgain.service.user.impl


import net.skillgain.domain.entity.user.UserInviteToken
import net.skillgain.domain.entity.user.UserRole
import net.skillgain.domain.mapper.user.toUser
import net.skillgain.domain.model.user.AuthProvider
import net.skillgain.domain.model.user.auth.*
import net.skillgain.exception.domain.user.invite.InviteTokenAlreadyUsedException
import net.skillgain.exception.domain.user.invite.InviteTokenExpiredException
import net.skillgain.exception.domain.user.password.PasswordChangeRequiredException
import net.skillgain.exception.domain.user.password.PasswordMismatchException
import net.skillgain.exception.domain.user.role.RoleNotFoundException
import net.skillgain.exception.domain.user.user.InvalidUserCredentialsException
import net.skillgain.exception.domain.user.user.UserAlreadyExistsException
import net.skillgain.persistence.repository.user.RoleRepository
import net.skillgain.security.jwt.JwtService
import net.skillgain.service.email.EmailService
import net.skillgain.service.user.AuthService
import net.skillgain.service.user.UserInviteTokenService
import net.skillgain.service.user.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class AuthServiceImpl(
    private val userService: UserService,
    private val roleRepository: RoleRepository,
    private val inviteTokenService: UserInviteTokenService,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val jwtService: JwtService
) : AuthService {

    override fun register(request: AuthRequest): String {
        if (userService.existsByEmail(request.email)) {
            throw UserAlreadyExistsException(request.email)
        }

        val roleUser = roleRepository.findByName(UserRole.ROLE_USER.name)
            ?: throw RoleNotFoundException(UserRole.ROLE_USER.name)

        val user = request.toUser(roleUser, passwordEncoder.encode(request.password))

        userService.save(user)

        return "User registered successfully"
    }

    override fun login(request: AuthRequest): AuthResponse {
        val user = userService.findByEmail(request.email) ?: throw InvalidUserCredentialsException()

        if (user.forcePasswordChange) {
            throw PasswordChangeRequiredException()
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw InvalidUserCredentialsException()
        }

        val token = jwtService.generateToken(user)
        return AuthResponse(token)
    }

    @Transactional
    override fun requestPasswordReset(request: PasswordResetRequest) {

        val user = userService.getByEmail(request.email) ?: return

        if (user.authProvider != AuthProvider.LOCAL) {
            return
        }

        val token = UUID.randomUUID().toString()

        val resetToken = UserInviteToken(
            token = token,
            user = user,
            expiresAt = LocalDateTime.now().plusHours(24)
        )

        inviteTokenService.save(resetToken)

        emailService.sendPasswordResetEmail(email = user.email, token = token)
    }

    @Transactional
    override fun finalizePassword(request: FinalizePasswordRequest): FinalizePasswordResponse {

        if (request.password != request.confirmPassword) {
            throw PasswordMismatchException()
        }

        val tokenEntity = inviteTokenService.findByToken(request.token)

        if (tokenEntity.used) {
            throw InviteTokenAlreadyUsedException()
        }

        if (tokenEntity.expiresAt.isBefore(LocalDateTime.now())) {
            throw InviteTokenExpiredException()
        }

        tokenEntity.user.apply {
            password = passwordEncoder.encode(request.password)
            forcePasswordChange = false
            emailVerified = true
        }

        tokenEntity.used = true

        return FinalizePasswordResponse(success = true, message = "Password set successfully.")
    }

}