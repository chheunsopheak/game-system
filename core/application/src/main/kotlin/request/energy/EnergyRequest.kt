package request.energy

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class EnergyRequest(
    @field:NotNull(message = "Value is required")
    @field:Min(value = 0, message = "Energy value must be at least 0")
    var value: Int,

    @field:NotBlank(message = "Merchant ID must not be blank")
    var merchantId: String,
    var isActive: Boolean = true,
)
