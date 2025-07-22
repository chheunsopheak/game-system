package response.energy

import entity.energy.EnergyEntity
import java.time.LocalDateTime

data class EnergyDetailResponse(
    var id: String,
    var value: Int,
    var merchantId: String,
    var merchantName: String,
    var isActive: Boolean = true,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {
    companion object {
        fun from(energy: EnergyEntity): EnergyDetailResponse = EnergyDetailResponse(
            id = energy.id,
            value = energy.value,
            merchantId = energy.merchant.id,
            merchantName = energy.merchant.name,
            isActive = energy.isActive,
            createdAt = energy.createdAt,
            updatedAt = energy.updatedAt
        )
    }
}
