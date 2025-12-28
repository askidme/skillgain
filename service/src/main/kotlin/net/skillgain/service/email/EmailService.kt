package net.skillgain.service.email

interface EmailService {

    fun sendUserInvite(email: String, token: String)
}