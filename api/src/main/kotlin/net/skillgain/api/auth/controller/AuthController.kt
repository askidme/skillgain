package net.skillgain.api.auth.controller

import jakarta.validation.Valid
import net.skillgain.domain.auth.AuthRequest
import net.skillgain.domain.auth.AuthResponse
import net.skillgain.service.auth.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@Valid @Validated @RequestBody req: AuthRequest): ResponseEntity<String> =
        ResponseEntity.ok(authService.register(req))

    @PostMapping("/login")
    fun login(@RequestBody req: AuthRequest): ResponseEntity<AuthResponse> =
        ResponseEntity.ok(authService.login(req))
}