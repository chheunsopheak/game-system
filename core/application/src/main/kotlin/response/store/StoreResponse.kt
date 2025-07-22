package response.store

import entity.store.StoreEntity
import java.time.LocalDateTime

data class StoreResponse(
    val id: String,
    val merchantId: String,
    val merchantName: String,
    var name: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAd: LocalDateTime
) {
    companion object {
        fun from(store: StoreEntity): StoreResponse = StoreResponse(
            id = store.id,
            merchantId = store.merchant.id,
            merchantName = store.merchant.name,
            name = store.name,
            isActive = store.isActive,
            createdAt = store.createdAt,
            updatedAd = store.updatedAt
        )
    }
}
