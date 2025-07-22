package request.energy

import common.OperationEnum
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class EnergyConsumeRequest(
    @field:NotBlank(message = "Merchant ID must not be blank")
    var merchantId: String,

    @field:NotBlank(message = "User ID must not be blank")
    val userId: String,

    @field:NotNull(message = "Energy amount must not be null")
    @field:Min(value = 1, message = "Energy must be at least 1")
    var energy: Int,

    @field:NotNull(message = "Operation must not be null")
    val operation: OperationEnum
)
