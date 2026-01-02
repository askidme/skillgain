package net.skillgain.api.auth.controller.user

import jakarta.validation.Valid
import net.skillgain.domain.model.user.profile.ChangePasswordRequest
import net.skillgain.domain.model.user.profile.UpdateUserProfileRequest
import net.skillgain.domain.model.user.profile.UserProfileResponse
import net.skillgain.security.auth.CustomUserPrincipal
import net.skillgain.service.user.UserProfileService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users/profile")
class UserProfileController(private val userProfileService: UserProfileService) {

    @PutMapping
    fun updateProfile(
        @Valid @RequestBody request: UpdateUserProfileRequest,
        @AuthenticationPrincipal principal: CustomUserPrincipal
    ): UserProfileResponse = userProfileService.updateProfile(principal.userId, request)

    @PostMapping("/password")
    fun changePassword(
        @Valid @RequestBody request: ChangePasswordRequest,
        @AuthenticationPrincipal principal: CustomUserPrincipal
    ) = userProfileService.resetPassword(principal.userId, request)
}