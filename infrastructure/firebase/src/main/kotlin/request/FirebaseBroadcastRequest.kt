package request

import jakarta.validation.constraints.NotBlank

data class FirebaseBroadcastRequest(
    @field:NotBlank(message = "Topic is required")
    val topic: String,

    @field:NotBlank(message = "Title is required")
    val title: String,

    @field:NotBlank(message = "Body is required")
    val body: String,

    val data: Map<String, String> = emptyMap()
)