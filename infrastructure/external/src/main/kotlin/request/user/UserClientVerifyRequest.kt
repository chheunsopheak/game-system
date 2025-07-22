package request.user

data class UserClientVerifyRequest(
    var phone: String,
    var code: String,
)
