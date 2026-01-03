package net.skillgain.service.user.impl


import net.skillgain.domain.entity.user.UserInviteToken
import net.skillgain.domain.entity.user.UserPasswordHistory
import net.skillgain.domain.entity.user.UserRole
import net.skillgain.domain.mapper.user.toUser
import net.skillgain.domain.model.user.AuthProvider
import net.skillgain.domain.model.user.auth.*
import net.skillgain.exception.domain.user.InviteTokenException
import net.skillgain.exception.domain.user.PasswordException
import net.skillgain.exception.domain.user.UserException
import net.skillgain.exception.domain.user.UserRoleException
import net.skillgain.exception.domain.user.code.InviteTokenExceptionCode
import net.skillgain.exception.domain.user.code.PasswordExceptionCode
import net.skillgain.exception.domain.user.code.UserExceptionCode
import net.skillgain.exception.domain.user.code.UserRoleExceptionCode
import net.skillgain.persistence.repository.user.RoleRepository
import net.skillgain.persistence.repository.user.UserPasswordHistoryRepository
import net.skillgain.security.jwt.JwtService
import net.skillgain.service.email.EmailService
import net.skillgain.service.user.AuthService
import net.skillgain.service.user.PasswordPolicyService
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
    private val jwtService: JwtService,
    private val passwordPolicyService: PasswordPolicyService,
    private val passwordHistoryRepository: UserPasswordHistoryRepository
) : AuthService {

    override fun register(request: AuthRequest): String {
        if (userService.existsByEmail(request.email)) {
            throw UserException(UserExceptionCode.EMAIL_ALREADY_EXISTS, arrayOf(request.email))
        }

        passwordPolicyService.validate(request.password)

        val roleUser = roleRepository.findByName(UserRole.ROLE_USER.name)
            ?: throw UserRoleException(UserRoleExceptionCode.ROLE_NOT_FOUND, arrayOf(UserRole.ROLE_USER.name))

        val encodedPassword = passwordEncoder.encode(request.password)
        val user = request.toUser(roleUser, encodedPassword)

        userService.save(user)

        passwordHistoryRepository.save(UserPasswordHistory(user = user, passwordHash = encodedPassword))

        return "User registered successfully"
    }

    override fun login(request: AuthRequest): AuthResponse {
        val user = userService.findByEmail(request.email)

        if (!user.active) {
            throw UserException(UserExceptionCode.USER_DISABLED, arrayOf(request.email))
        }

        if (user.forcePasswordChange) {
            throw PasswordException(PasswordExceptionCode.PASSWORD_CHANGE_REQUIRED)
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw UserException(UserExceptionCode.INVALID_USER_CREDENTIALS)
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
            throw PasswordException(PasswordExceptionCode.PASSWORD_MISMATCH)
        }

        passwordPolicyService.validate(request.password)

        val tokenEntity = inviteTokenService.findByToken(request.token)

        if (tokenEntity.used) {
            throw InviteTokenException(InviteTokenExceptionCode.INVITE_TOKEN_ALREADY_USED)
        }

        if (tokenEntity.expiresAt.isBefore(LocalDateTime.now())) {
            throw InviteTokenException(InviteTokenExceptionCode.INVITE_TOKEN_EXPIRED)
        }

        passwordPolicyService.validateNotReused(tokenEntity.user, request.password)

        val encodedPassword = passwordEncoder.encode(request.password)

        tokenEntity.user.apply {
            password = encodedPassword
            forcePasswordChange = false
            emailVerified = true
        }

        passwordHistoryRepository.save(UserPasswordHistory(user = tokenEntity.user, passwordHash = encodedPassword))

        tokenEntity.used = true

        return FinalizePasswordResponse(success = true, message = "Password set successfully.")
    }

}