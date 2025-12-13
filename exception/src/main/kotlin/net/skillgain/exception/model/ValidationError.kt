package net.skillgain.exception.model

data class ValidationError(
    val field: String,
    val message: String,
    val rejectedValue: Any?
)