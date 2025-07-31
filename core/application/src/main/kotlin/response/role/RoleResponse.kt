package response.role

import entity.role.RoleEntity
import java.time.LocalDateTime

data class RoleResponse(
    val id: String,
    val name: String,
    val code: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(role: RoleEntity): RoleResponse = RoleResponse(
            id = role.id,
            name = role.name,
            code = role.code,
            description = role.description,
            isActive = role.isActive,
            createdAt = role.createdAt,
            updatedAt = role.updatedAt
        )
    }
}