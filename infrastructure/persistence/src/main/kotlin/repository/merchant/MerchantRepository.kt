package repository.merchant

import entity.merchant.MerchantEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface MerchantRepository : BaseRepository<MerchantEntity, String> {
    fun findByUserId(userId: String): MerchantEntity?
    fun findMerchantById(id: String): MerchantEntity?
    fun findByEmail(email: String): MerchantEntity?
    fun findByPhoneNumber(phoneNumber: String): MerchantEntity?
}