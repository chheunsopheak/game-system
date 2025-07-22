package response.merchant

import entity.merchant.MerchantEntity
import java.time.LocalDateTime

data class MerchantResponse(
    val id: String,
    val name: String,
    val phone: String? = null,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(merchant: MerchantEntity): MerchantResponse = MerchantResponse(
            id = merchant.id,
            name = merchant.name,
            phone = merchant.phoneNumber,
            isActive = merchant.isActive,
            createdAt = merchant.createdAt,
            updatedAt = merchant.updatedAt
        )
    }
}
