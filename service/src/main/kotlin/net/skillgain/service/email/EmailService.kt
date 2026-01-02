package net.skillgain.service.email

interface EmailService {

    fun sendUserInvite(email: String, token: String)
    fun sendPasswordResetEmail(email: String, token: String)
}