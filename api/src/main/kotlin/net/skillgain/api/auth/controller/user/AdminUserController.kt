package net.skillgain.api.auth.controller.user

import net.skillgain.domain.entity.user.toResponse
import net.skillgain.domain.model.user.admin.CreateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRolesRequest
import net.skillgain.domain.model.user.admin.UserResponse
import net.skillgain.service.user.UserAdminService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
class AdminUserController(
    private val userAdminService: UserAdminService
) {

    @PostMapping
    fun create(@RequestBody request: CreateUserRequest): UserResponse =
        userAdminService.createUser(request).toResponse()

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateUserRequest
    ): UserResponse =
        userAdminService.updateUser(id, request).toResponse()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        userAdminService.deleteUser(id)
    }

    @PutMapping("/{id}/roles")
    fun updateRoles(
        @PathVariable id: Long,
        @RequestBody request: UpdateUserRolesRequest,
        authentication: Authentication
    ): UserResponse {
        val adminId = authentication.principal as Long // or extracted from JWT
        return userAdminService.updateUserRoles(adminId, id, request).toResponse()
    }

    @GetMapping
    fun list(): List<UserResponse> =
        userAdminService.listUsers().map { it.toResponse() }
}
