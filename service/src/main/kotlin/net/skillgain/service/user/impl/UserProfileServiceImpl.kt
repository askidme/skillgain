package net.skillgain.service.user.impl

import net.skillgain.domain.mapper.user.toProfileResponse
import net.skillgain.domain.mapper.user.toUser
import net.skillgain.domain.model.user.profile.ChangePasswordRequest
import net.skillgain.domain.model.user.profile.UpdateUserProfileRequest
import net.skillgain.domain.model.user.profile.UserProfileResponse
import net.skillgain.exception.domain.user.password.PasswordMismatchException
import net.skillgain.exception.domain.user.user.InvalidUserCredentialsException
import net.skillgain.exception.domain.user.user.UserNotFoundException
import net.skillgain.persistence.repository.user.UserRepository
import net.skillgain.service.user.UserProfileService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserProfileServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserProfileService {

    @Transactional
    override fun updateProfile(email: String, request: UpdateUserProfileRequest): UserProfileResponse {
        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException(email)

        return userRepository.save(request.toUser(user)).toProfileResponse()
    }

    @Transactional
    override fun changePassword(email: String, request: ChangePasswordRequest) {

        if (request.newPassword != request.confirmPassword) {
            throw PasswordMismatchException()
        }

        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException(email)

        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw InvalidUserCredentialsException()
        }

        userRepository.save(
            user.copy(
                password = passwordEncoder.encode(request.currentPassword),
                forcePasswordChange = false
            )
        )
    }
}