package net.skillgain.api.auth.controller.user

import jakarta.validation.Valid
import net.skillgain.domain.model.user.auth.*
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

    @PostMapping("/password/reset")
    fun requestPasswordReset(@Valid @RequestBody request: PasswordResetRequest): ResponseEntity<Void> {
        authService.requestPasswordReset(request)
        return ok().build()
    }

    @PostMapping("/password/finalize")
    fun finalizePassword(@Valid @RequestBody request: FinalizePasswordRequest): ResponseEntity<FinalizePasswordResponse> =
        ok(authService.finalizePassword(request))

}