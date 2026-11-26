package rocks.drnd.whereisivan.route

import io.github.drndivoje.vluent.api.ValidationResult
import io.github.drndivoje.vluent.api.Validator

class StartActivityRequestValidator : Validator<StartActivityRequest> {

    override fun validate(input: StartActivityRequest): ValidationResult {
        val errors = mutableListOf<String>()
        if (input.startTime <= 0) {
            errors.add("Start time must be a positive value in milliseconds")
        }
        return if (errors.isEmpty()) {
            ValidationResult.SUCCESS
        } else {
            ValidationResult.createError(errors.joinToString("; "))
        }
    }
}