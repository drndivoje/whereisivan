package rocks.drnd.whereisivan.service

import io.github.drndivoje.vluent.api.ValidationResult
import io.github.drndivoje.vluent.api.Validator
import rocks.drnd.whereisivan.route.LocationTrackingRequest

class LocationTrackingRequestValidator : Validator<List<LocationTrackingRequest>> {
    override fun validate(input: List<LocationTrackingRequest>): ValidationResult {
        val errors = mutableListOf<String>()
        for ((_, location) in input.withIndex()) {
            if (location.latitude !in -90.0..90.0) {
                errors.add("Latitude must be between -90 and 90")
            }
            if (location.longitude !in -180.0..180.0) {
                errors.add("Longitude must be between -180 and 180")
            }
        }
        return if (errors.isEmpty()) {
            ValidationResult.SUCCESS
        } else {
            ValidationResult.createError(errors.joinToString("; "))
        }
    }
}