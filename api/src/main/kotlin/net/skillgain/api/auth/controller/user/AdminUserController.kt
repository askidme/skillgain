package net.skillgain.api.auth.controller.user

import jakarta.validation.Valid
import net.skillgain.domain.model.user.admin.CreateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRolesRequest
import net.skillgain.domain.model.user.admin.UserResponse
import net.skillgain.security.auth.CustomUserPrincipal
import net.skillgain.service.user.UserAdminService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
class AdminUserController(
    private val userAdminService: UserAdminService
) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> =
        status(HttpStatus.CREATED).body(userAdminService.createUser(request))

    @PatchMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: UpdateUserRequest): UserResponse =
        userAdminService.updateUser(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
        ResponseEntity.noContent().also { userAdminService.deleteUser(id) }.build()


    @PutMapping("/{id}/roles")
    fun updateRoles(
        @PathVariable id: Long,
        @RequestBody request: UpdateUserRolesRequest,
        authentication: Authentication
    ): UserResponse {
        val adminId = (authentication.principal as CustomUserPrincipal).userId
        return userAdminService.updateUserRoles(adminId, id, request)
    }

    @GetMapping
    fun list(): List<UserResponse> = userAdminService.listUsers()
}
