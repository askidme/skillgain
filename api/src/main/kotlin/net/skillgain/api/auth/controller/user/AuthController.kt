package net.skillgain.api.auth.controller.user

import jakarta.validation.Valid
import net.skillgain.domain.model.user.auth.AuthRequest
import net.skillgain.domain.model.user.auth.AuthResponse
import net.skillgain.domain.model.user.invite.AcceptInviteRequest
import net.skillgain.domain.model.user.invite.AcceptInviteResponse
import net.skillgain.service.user.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@Valid @Validated @RequestBody req: AuthRequest): ResponseEntity<String> =
        ok(authService.register(req))

    @PostMapping("/login")
    fun login(@RequestBody req: AuthRequest): ResponseEntity<AuthResponse> =
        ok(authService.login(req))

    @PostMapping("invite/accept")
    fun acceptInvite(@RequestBody request: AcceptInviteRequest): ResponseEntity<AcceptInviteResponse> =
        ok(authService.acceptInvite(request))
}