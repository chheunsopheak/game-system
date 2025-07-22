package response.merchant

data class MyMerchantResponse(
    val userId: String,
    val userName: String,
    val merchantId: String,
    val merchantName: String,
    val merchantVerified: Boolean = false,
    val merchantLogo: String? = null,
    val merchantEmail: String,
    val merchantPhone: String,
    val merchantAddress: String,
    val totalTicket: Int
)