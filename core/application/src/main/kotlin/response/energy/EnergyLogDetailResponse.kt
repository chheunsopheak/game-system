package response.energy

import entity.energy.EnergyLogEntity
import java.time.LocalDateTime

data class EnergyLogDetailResponse(
    val id: String,
    val merchantId: String,
    val merchantName: String,
    val userId: String,
    val userName: String,
    val value: Int,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(energyLog: EnergyLogEntity): EnergyLogDetailResponse = EnergyLogDetailResponse(
            id = energyLog.id,
            merchantId = energyLog.merchant.id,
            merchantName = energyLog.merchant.name,
            userId = energyLog.user.id,
            userName = energyLog.user.name,
            value = energyLog.value,
            isActive = energyLog.isActive,
            createdAt = energyLog.createdAt,
            updatedAt = energyLog.updatedAt
        )
    }
}
