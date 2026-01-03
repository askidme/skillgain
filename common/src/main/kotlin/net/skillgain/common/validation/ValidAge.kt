package net.skillgain.common.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidAgeValidator::class])
annotation class ValidAge (
    val min: Int = 13,
    val max: Int = 120,

    val message: String = "{validation.user.birth_date.age_invalid}",

    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)