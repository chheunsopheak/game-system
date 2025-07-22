package request.user

data class UserChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
