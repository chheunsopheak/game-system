package request.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 4, max = 20, message = "Username must be 4-20 characters")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    val email: String,

    @field:NotBlank(message = "Name is required")
    val name: String,
    val isActive: Boolean = true
)
