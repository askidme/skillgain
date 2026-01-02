package net.skillgain.service.user

import net.skillgain.domain.model.user.profile.ChangePasswordRequest
import net.skillgain.domain.model.user.profile.UpdateUserProfileRequest
import net.skillgain.domain.model.user.profile.UserProfileResponse

interface UserProfileService {

    fun updateProfile(userId: Long, request: UpdateUserProfileRequest): UserProfileResponse

    fun changePassword(userId: Long, request: ChangePasswordRequest)
}