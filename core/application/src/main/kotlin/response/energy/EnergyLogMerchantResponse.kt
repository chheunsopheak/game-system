package response.energy

import entity.energy.EnergyLogEntity
import java.time.LocalDateTime

data class EnergyLogMerchantResponse(
    val id: String,
    val merchantId: String,
    val merchantName: String,
    val userId: String,
    val userName: String,
    val userProfile: String,
    val userPhone: String,
    val value: Int,
    val operation: Int,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(entity: EnergyLogEntity): EnergyLogMerchantResponse {
            return EnergyLogMerchantResponse(
                id = entity.id,
                merchantId = entity.merchant.id,
                merchantName = entity.merchant.name,
                userId = entity.user.id,
                userName = entity.user.name,
                userProfile = entity.user.photoUrl.toString(),
                userPhone = entity.user.phone,
                value = entity.value,
                operation = entity.note?.toInt() ?: 0,
                createdAt = entity.createdAt
            )
        }
    }
}
