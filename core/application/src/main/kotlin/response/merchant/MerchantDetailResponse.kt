package response.merchant

import java.time.LocalDateTime

data class MerchantDetailResponse(
    val id: String,
    val userId: String,
    val userName: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val description: String? = null,
    val logoUrl: String? = null,
    val coverUrl: String? = null,
    val energy: Int,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)