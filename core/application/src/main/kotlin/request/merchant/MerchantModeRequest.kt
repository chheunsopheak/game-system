package request.merchant

data class MerchantModeRequest(
    val userId: String,
    val merchantId: String,
    val storeId: String? = null,
    val mode: String
)
