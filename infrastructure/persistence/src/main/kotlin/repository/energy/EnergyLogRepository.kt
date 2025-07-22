package repository.energy

import entity.energy.EnergyLogEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface EnergyLogRepository : BaseRepository<EnergyLogEntity, String> {
    fun findAllByMerchantId(merchantId: String, pageable: Pageable): Page<EnergyLogEntity>
    fun findAllByUserId(userId: String, pageable: Pageable): Page<EnergyLogEntity>
}