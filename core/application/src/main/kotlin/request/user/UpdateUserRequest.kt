package request.user

data class UpdateUserRequest(
    val userId: String,
    val name: String,
    val photo: String,
    val phone: String,
    val role: Int
)
