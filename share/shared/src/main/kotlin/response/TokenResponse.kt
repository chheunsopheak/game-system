package response

data class TokenResponse(
    val token: String,
    val expiresIn: Long,
)