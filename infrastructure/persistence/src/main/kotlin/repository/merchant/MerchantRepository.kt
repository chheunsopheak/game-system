package repository.merchant

import entity.merchant.MerchantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantRepository : JpaRepository<MerchantEntity, String> {
}