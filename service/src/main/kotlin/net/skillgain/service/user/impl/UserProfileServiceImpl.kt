package net.skillgain.service.user.impl

import net.skillgain.domain.entity.user.UserPasswordHistory
import net.skillgain.domain.mapper.user.toProfileResponse
import net.skillgain.domain.mapper.user.toUser
import net.skillgain.domain.model.user.profile.ChangePasswordRequest
import net.skillgain.domain.model.user.profile.UpdateUserProfileRequest
import net.skillgain.domain.model.user.profile.UserProfileResponse
import net.skillgain.exception.domain.user.PasswordException
import net.skillgain.exception.domain.user.UserException
import net.skillgain.exception.domain.user.code.PasswordExceptionCode
import net.skillgain.exception.domain.user.code.UserExceptionCode
import net.skillgain.persistence.repository.user.UserPasswordHistoryRepository
import net.skillgain.service.user.PasswordPolicyService
import net.skillgain.service.user.UserProfileService
import net.skillgain.service.user.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserProfileServiceImpl(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val passwordHistoryRepository: UserPasswordHistoryRepository,
    private val passwordPolicyService: PasswordPolicyService
) : UserProfileService {

    @Transactional
    override fun updateProfile(userId: Long, request: UpdateUserProfileRequest): UserProfileResponse {
        val user = userService.findById(userId)

        return userService.save(request.toUser(user)).toProfileResponse()
    }

    @Transactional
    override fun changePassword(userId: Long, request: ChangePasswordRequest) {

        if (request.newPassword != request.confirmPassword) {
            throw PasswordException(PasswordExceptionCode.PASSWORD_MISMATCH)
        }

        passwordPolicyService.validate(request.confirmPassword)

        val user = userService.findById(userId)

        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw UserException(UserExceptionCode.INVALID_USER_CREDENTIALS)
        }

        passwordPolicyService.validateNotReused(user, request.newPassword)

        val encodedPassword = passwordEncoder.encode(request.currentPassword)
        userService.save(
            user.apply {
                password = encodedPassword
                forcePasswordChange = false
            }
        )
        passwordHistoryRepository.save(UserPasswordHistory(user = user,passwordHash = encodedPassword))
    }
}