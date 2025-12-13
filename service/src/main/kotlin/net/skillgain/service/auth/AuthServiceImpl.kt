package net.skillgain.service.auth


import net.skillgain.domain.auth.AuthRequest
import net.skillgain.domain.auth.AuthResponse
import net.skillgain.domain.model.Role
import net.skillgain.domain.model.User
import net.skillgain.exception.domain.user.InvalidUserCredentialsException
import net.skillgain.persistence.repository.UserRepository
import net.skillgain.security.jwt.JwtService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) : AuthService {

    override fun register(request: AuthRequest): String {
        if (userRepository.findByUsername(request.username) != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Username already exists")
        }

        val user = User(
            username = request.username,
            email = "${request.username}@skillgain.net",
            password = passwordEncoder.encode(request.password),
            role = Role.USER
        )

        userRepository.save(user)
        return "User registered successfully"
    }

    override fun login(request: AuthRequest): AuthResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw InvalidUserCredentialsException()

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw InvalidUserCredentialsException()
        }

        val token = jwtService.generateToken(user)
        return AuthResponse(token)
    }
}