package net.skillgain.service.user

import net.skillgain.domain.model.user.profile.ChangePasswordRequest
import net.skillgain.domain.model.user.profile.UpdateUserProfileRequest
import net.skillgain.domain.model.user.profile.UserProfileResponse

interface UserProfileService {

    fun updateProfile(email: String, request: UpdateUserProfileRequest): UserProfileResponse

    fun changePassword(email: String, request: ChangePasswordRequest)
}