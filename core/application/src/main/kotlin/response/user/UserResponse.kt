package response.user

import entity.user.UserEntity

data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val name: String,
    val photo: String?,
    val phone: String?,
    val isActive: Boolean,
    var energy: Int?
) {
    companion object {
        fun from(user: UserEntity): UserResponse = UserResponse(
            id = user.id,
            username = user.userName,
            email = user.email,
            name = user.name,
            isActive = user.isActive,
            photo = user.photo,
            phone = user.phone,
            energy = user.energy
        )
    }
}
