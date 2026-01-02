package net.skillgain.service.user.impl

import net.skillgain.domain.mapper.user.toProfileResponse
import net.skillgain.domain.mapper.user.toUser
import net.skillgain.domain.model.user.profile.ChangePasswordRequest
import net.skillgain.domain.model.user.profile.UpdateUserProfileRequest
import net.skillgain.domain.model.user.profile.UserProfileResponse
import net.skillgain.exception.domain.user.password.PasswordMismatchException
import net.skillgain.exception.domain.user.user.InvalidUserCredentialsException
import net.skillgain.exception.domain.user.user.UserNotFoundException
import net.skillgain.service.user.UserProfileService
import net.skillgain.service.user.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserProfileServiceImpl(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) : UserProfileService {

    @Transactional
    override fun updateProfile(userId: Long, request: UpdateUserProfileRequest): UserProfileResponse {
        val user = userService.findById(userId)

        return userService.save(request.toUser(user)).toProfileResponse()
    }

    @Transactional
    override fun resetPassword(userId: Long, request: ChangePasswordRequest) {

        if (request.newPassword != request.confirmPassword) {
            throw PasswordMismatchException()
        }

        val user = userService.findById(userId)

        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw InvalidUserCredentialsException()
        }

        userService.save(
            user.apply {
                password = passwordEncoder.encode(request.currentPassword)
                forcePasswordChange = false
            }
        )
    }
}