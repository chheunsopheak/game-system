package request.auth

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)