package response.user

import entity.user.UserEntity
import java.time.LocalDateTime

data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val name: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(user: UserEntity): UserResponse = UserResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            name = user.name,
            isActive = user.isActive,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}
