package net.skillgain.service.email.impl

import net.skillgain.service.email.EmailService
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl: EmailService {
    override fun sendUserInvite(email: String, token: String) {
        println("emailing token $token to $email ...")
    }
}