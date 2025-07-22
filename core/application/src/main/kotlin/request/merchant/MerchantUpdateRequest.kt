package request.merchant

data class MerchantUpdateRequest(
    var name: String,
    var email: String,
    var phone: String,
    var address: String? = null,
    var description: String? = null,
    var logoUrl: String? = null,
    var coverUrl: String? = null,
    val energy: Int,
    var isActive: Boolean
)
