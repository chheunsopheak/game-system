package request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class FirebaseRequest(
    @field:NotEmpty(message = "At least one device token is required")
    val token: List<String>,

    @field:NotBlank(message = "Title is required")
    val title: String,

    @field:NotBlank(message = "Body is required")
    val body: String,

    val data: Map<String, String> = emptyMap()
)