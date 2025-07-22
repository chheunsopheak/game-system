package request.user

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class LoginRequest(
    @NotNull(message = "Username can not be null.")
    @NotEmpty(message = "Username can not be empty.")
    val username: String,

    @NotNull(message = "Password can not be null.")
    @NotEmpty(message = "Password can not be empty.")
    val password: String
)