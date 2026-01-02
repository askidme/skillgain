package net.skillgain.service.user.impl


import net.skillgain.domain.entity.user.User
import net.skillgain.exception.domain.user.user.UserNotFoundException
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
        userRepository.findById(userId).orElseThrow { UserNotFoundException(userId) }

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): User = userRepository.findByEmail(email)
        ?: throw UserNotFoundException(email)

    @Transactional(readOnly = true)
    override fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)

    override fun save(user: User): User = userRepository.save(user)

    override fun delete(user: User) = userRepository.delete(user)

    override fun delete(userId: Long) = userRepository.deleteById(userId)

}
