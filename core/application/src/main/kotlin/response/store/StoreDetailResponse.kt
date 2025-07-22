package response.store

import entity.store.StoreEntity
import java.time.LocalDateTime

data class StoreDetailResponse(
    val id: String,
    val merchantId: String,
    val merchantName: String,
    var name: String,
    val location: String? = null,
    val managerName: String? = null,
    val contactNumber: String? = null,
    val openHours: LocalDateTime? = null,
    val closeHours: LocalDateTime? = null,
    val description: String? = null,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(store: StoreEntity): StoreDetailResponse = StoreDetailResponse(
            id = store.id,
            merchantId = store.merchant.id,
            merchantName = store.merchant.name,
            name = store.name,
            location = store.location,
            managerName = store.managerName,
            contactNumber = store.contactNumber,
            openHours = store.openHours,
            closeHours = store.closeHours,
            description = store.description,
            isActive = store.isActive,
            createdAt = store.createdAt,
            updatedAt = store.updatedAt
        )
    }
}