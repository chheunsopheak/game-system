package request.merchant

data class MerchantRequest(
    var name: String,
    var email: String,
    val password: String,
    var phone: String,
    var address: String? = null,
    var description: String? = null,
    var logoUrl: String? = null,
    var coverUrl: String? = null,
    var isActive: Boolean
)
