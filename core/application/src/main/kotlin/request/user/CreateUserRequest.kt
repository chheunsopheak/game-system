package request.user

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class CreateUserRequest(
    @NotNull(message = "Username can not be null.")
    @NotEmpty(message = "Username can not be empty.")
    val username: String,
    val email: String,
    val password: String,
    val name: String,
    val photo: String,
    val phone: String,
    val role: Int
)
