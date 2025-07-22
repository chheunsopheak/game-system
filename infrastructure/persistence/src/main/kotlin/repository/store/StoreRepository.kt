package repository.store

import entity.store.StoreEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface StoreRepository : BaseRepository<StoreEntity, String> {
    fun existsByNameAndMerchantId(name: String, merchantId: String): Boolean
    fun findAllByMerchantId(merchantId: String): List<StoreEntity>
}