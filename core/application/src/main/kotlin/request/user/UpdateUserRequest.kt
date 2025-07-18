package request.user

data class UpdateUserRequest(
    val id: String,
    val username: String? = null,
    val email: String? = null,
    val name: String? = null,
    val isActive: Boolean
)
