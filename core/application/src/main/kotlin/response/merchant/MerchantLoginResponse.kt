package response.merchant

data class MerchantLoginResponse(
    val userId: String,
    val merchantId: String,
    val storeId: String?,
    val mode: String,
    val accessToken: String,
    val expiresIn: Long,
    val tokenType: String,
    val role: String
)
