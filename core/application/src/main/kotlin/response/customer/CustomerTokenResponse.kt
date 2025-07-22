package response.customer

data class CustomerTokenResponse(
    val userId: String,
    val accessToken: String,
    val expiresIn: Long,
    val tokenType: String,
    val role: String,
    val userQr: String
)