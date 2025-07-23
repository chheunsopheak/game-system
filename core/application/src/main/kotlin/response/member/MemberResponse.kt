package response.member

import java.time.LocalDateTime

data class MemberResponse(
    val id: String,
    val name: String,
    val phone: String,
    val ticket: Int,
    val rewardType: String,
    val email: String?,
    val createdAt: LocalDateTime
)
