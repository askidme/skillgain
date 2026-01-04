package net.skillgain.api.auth.controller.user

import jakarta.validation.Valid
import net.skillgain.domain.mapper.toPagedResponse
import net.skillgain.domain.model.PagedResponse
import net.skillgain.domain.model.user.admin.*
import net.skillgain.security.auth.CustomUserPrincipal
import net.skillgain.service.user.UserAdminService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    fun delete(@PathVariable id: Long, @AuthenticationPrincipal principal: CustomUserPrincipal): ResponseEntity<Void> =
        ResponseEntity.noContent().also { userAdminService.deleteUser(principal.userId, id) }.build()


    @PutMapping("/{id}/roles")
    fun updateRoles(
        @PathVariable id: Long,
        @RequestBody request: UpdateUserRolesRequest,
        @AuthenticationPrincipal principal: CustomUserPrincipal
    ): UserResponse = userAdminService.updateUserRoles(principal.userId, id, request)

    @GetMapping
    fun list( pageable: Pageable): PagedResponse<UserResponse> = userAdminService.listUsers(pageable).toPagedResponse()

    @PutMapping("/{id}/restore")
    fun restoreUser(@PathVariable id: Long): UserResponse = userAdminService.restoreUser(id)

    @PutMapping("/{id}/activation")
    fun updateActivation(@PathVariable id: Long, @RequestBody request: UpdateUserActivationRequest): UserResponse =
        userAdminService.updateActivation(id, request)
}
