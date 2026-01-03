package net.skillgain.service.user.impl


import net.skillgain.domain.entity.user.User
import net.skillgain.exception.domain.user.UserException
import net.skillgain.exception.domain.user.code.UserExceptionCode
import net.skillgain.persistence.repository.user.UserRepository
import net.skillgain.service.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun findAll(): List<User> {
        return userRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun findById(userId: Long): User =
        userRepository.findByIdAndDeletedAtIsNull(userId) ?: throw UserException(
            UserExceptionCode.USER_ID_NOT_FOUND,
            arrayOf(userId)
        )

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): User = userRepository.findByEmailAndDeletedAtIsNull(email)
        ?: throw UserException(UserExceptionCode.USER_EMAIL_NOT_FOUND, arrayOf(email))

    @Transactional(readOnly = true)
    override fun getByEmail(email: String): User? = userRepository.findByEmailAndDeletedAtIsNull(email)

    @Transactional(readOnly = true)
    override fun existsByEmail(email: String): Boolean = userRepository.existsByEmailAndDeletedAtIsNull(email)

    override fun save(user: User): User = userRepository.save(user)

    override fun delete(adminUserId: Long, user: User) = user.markDeleted(adminUserId)

    override fun delete(adminUserId: Long, userId: Long) {
        val user = userRepository.findById(userId).orElseThrow {
            UserException(
                UserExceptionCode.USER_ID_NOT_FOUND,
                arrayOf(userId)
            )
        }
        user.markDeleted(adminUserId)
    }

    override fun findByIdIncludingDeleted(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { UserException(UserExceptionCode.USER_ID_NOT_FOUND, arrayOf(userId)) }
    }

}
