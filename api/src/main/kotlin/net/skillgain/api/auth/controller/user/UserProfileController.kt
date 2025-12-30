package net.skillgain.api.auth.controller.user

import jakarta.validation.Valid
import net.skillgain.domain.model.user.profile.ChangePasswordRequest
import net.skillgain.domain.model.user.profile.UpdateUserProfileRequest
import net.skillgain.domain.model.user.profile.UserProfileResponse
import net.skillgain.service.user.AuthService
import net.skillgain.service.user.UserProfileService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users/profile")
class UserProfileController(private val userProfileService: UserProfileService) {

    @PutMapping
    fun updateProfile(
        @Valid @RequestBody request: UpdateUserProfileRequest,
        authentication: Authentication
    ): UserProfileResponse {
        return userProfileService.updateProfile(authentication.name, request)
    }

    @PostMapping("/password")
    fun changePassword(
        @Valid @RequestBody request: ChangePasswordRequest,
        authentication: Authentication
    ) {
        userProfileService.changePassword(authentication.name, request)
    }
}