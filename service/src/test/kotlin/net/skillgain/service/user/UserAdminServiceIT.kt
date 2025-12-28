package net.skillgain.service.user

import net.skillgain.domain.model.user.AuthProvider
import net.skillgain.domain.model.user.admin.CreateUserRequest
import net.skillgain.service.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class UserAdminServiceIT: AbstractIntegrationTest() {

    @Autowired
    private lateinit var userAdminService: UserAdminService

    @Test
    fun `should create a user by admin successfully`() {
        //Arrange
        val userRequest = CreateUserRequest(email = "admintest@skillgain.com", firstName = "User", lastName = "Administrator")

        //Act
        val user = userAdminService.createUser(userRequest)

        //Assert
        assertThat(user).isNotNull
        assertThat(user.email).isEqualTo("admintest@skillgain.com")
        assertThat(user.firstName).isEqualTo("User")
        assertThat(user.lastName).isEqualTo("Administrator")
        assertThat(user.authProvider).isEqualTo(AuthProvider.LOCAL.name)
        assertThat(user.active).isTrue

    }

    @Test
    fun `should get all users by admin successfully`() {

        //Act
        val users = userAdminService.listUsers()

        //Assert
        assertThat(users).isNotEmpty
        assertThat(users.count()).isEqualTo(1)
        assertThat(users).anyMatch { it.email == "admin@skillgain.net" }
    }
}