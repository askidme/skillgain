package net.skillgain.common.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate
import java.time.Period

class ValidAgeValidator : ConstraintValidator<ValidAge, LocalDate?> {

    private var min: Int = 0
    private var max: Int = 0

    override fun initialize(annotation: ValidAge) {
        min = annotation.min
        max = annotation.max
    }

    override fun isValid(
        value: LocalDate?,
        context: ConstraintValidatorContext
    ): Boolean {
        // Allow null for partial updates
        if (value == null) return true

        val today = LocalDate.now()

        if (value.isAfter(today)) {
            return false
        }

        val age = Period.between(value, today).years
        return age in min..max
    }
}
