package net.skillgain.service.user


import net.skillgain.domain.entity.user.User
import net.skillgain.domain.model.user.AuthRequest
import net.skillgain.domain.model.user.AuthResponse
import net.skillgain.exception.domain.user.InvalidUserCredentialsException
import net.skillgain.exception.domain.user.UserAlreadyExistsException
import net.skillgain.persistence.repository.user.UserRepository
import net.skillgain.security.jwt.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) : AuthService {

    override fun register(request: AuthRequest): String {
        if (userRepository.findByEmail(request.email) != null) {
            throw UserAlreadyExistsException(request.email)
        }

        val user = User.signUp(email = request.email, password = passwordEncoder.encode(request.password))

        userRepository.save(user)
        return "User registered successfully"
    }

    override fun login(request: AuthRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw InvalidUserCredentialsException()

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw InvalidUserCredentialsException()
        }

        val token = jwtService.generateToken(user)
        return AuthResponse(token)
    }
}