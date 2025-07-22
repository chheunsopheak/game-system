package response.energy

import entity.energy.EnergyEntity
import java.time.LocalDateTime

data class EnergyResponse(
    var id: String,
    var merchantName: String,
    var value: Int,
    var isActive: Boolean = true,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {
    companion object {
        fun from(energy: EnergyEntity): EnergyResponse = EnergyResponse(
            id = energy.id,
            merchantName = energy.merchant.name,
            value = energy.value,
            isActive = energy.isActive,
            createdAt = energy.createdAt,
            updatedAt = energy.updatedAt
        )
    }
}
