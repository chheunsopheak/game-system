package response.user

data class UserClientResult(
    val token: String,
    val refreshToken: String,
    val expired: Long
)
