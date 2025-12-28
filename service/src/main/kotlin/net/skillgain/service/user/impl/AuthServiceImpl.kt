package net.skillgain.service.user.impl


import net.skillgain.domain.entity.user.User
import net.skillgain.domain.entity.user.UserRole
import net.skillgain.domain.model.user.auth.AuthRequest
import net.skillgain.domain.model.user.auth.AuthResponse
import net.skillgain.domain.model.user.invite.AcceptInviteRequest
import net.skillgain.domain.model.user.invite.AcceptInviteResponse
import net.skillgain.exception.domain.user.invite.InvalidInviteTokenException
import net.skillgain.exception.domain.user.invite.InviteTokenAlreadyUsedException
import net.skillgain.exception.domain.user.invite.InviteTokenExpiredException
import net.skillgain.exception.domain.user.user.InvalidUserCredentialsException
import net.skillgain.exception.domain.user.password.PasswordChangeRequiredException
import net.skillgain.exception.domain.user.password.PasswordMismatchException
import net.skillgain.exception.domain.user.role.RoleNotFoundException
import net.skillgain.exception.domain.user.user.UserAlreadyExistsException
import net.skillgain.persistence.repository.user.InviteTokenRepository
import net.skillgain.persistence.repository.user.RoleRepository
import net.skillgain.persistence.repository.user.UserRepository
import net.skillgain.security.jwt.JwtService
import net.skillgain.service.user.AuthService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val inviteTokenRepository: InviteTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) : AuthService {

    override fun register(request: AuthRequest): String {
        if (userRepository.findByEmail(request.email) != null) {
            throw UserAlreadyExistsException(request.email)
        }

        val roleUser = roleRepository.findByName(UserRole.ROLE_USER.name)
            ?: throw RoleNotFoundException(UserRole.ROLE_USER.name)

        val user = User.signUp(
            email = request.email,
            encodedPassword = passwordEncoder.encode(request.password),
            defaultRole = roleUser
        )

        userRepository.save(user)
        return "User registered successfully"
    }

    override fun login(request: AuthRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email) ?: throw InvalidUserCredentialsException()

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
    override fun acceptInvite(request: AcceptInviteRequest): AcceptInviteResponse {

        val invite = inviteTokenRepository.findByToken(request.token)
            ?: throw InvalidInviteTokenException()

        if (invite.used) {
            throw InviteTokenAlreadyUsedException()
        }

        if (invite.expiresAt.isBefore(LocalDateTime.now())) {
            throw InviteTokenExpiredException()
        }

        if (request.password != request.confirmPassword) {
            throw PasswordMismatchException()
        }

        val user = invite.user.copy(
            password = passwordEncoder.encode(request.password),
            forcePasswordChange = false,
            emailVerified = true
        )

        invite.used = true

        userRepository.save(user)
        inviteTokenRepository.save(invite)

        return AcceptInviteResponse(
            email = request.token,
            activated = true,
            message = "Your account has been activated successfully."
        )
    }

}