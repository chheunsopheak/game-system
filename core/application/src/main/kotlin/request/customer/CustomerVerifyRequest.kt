package request.customer

data class CustomerVerifyRequest(
    var phone: String,
    var code: String
)
