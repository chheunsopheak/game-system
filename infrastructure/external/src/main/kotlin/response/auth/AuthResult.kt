package response.auth

data class AuthResult(
    val token: String,
    val refreshToken: String,
    val expired: Long
)
