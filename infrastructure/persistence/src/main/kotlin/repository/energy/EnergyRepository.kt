package repository.energy

import entity.energy.EnergyEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface EnergyRepository : BaseRepository<EnergyEntity, String> {
    fun findByMerchant_Id(merchantId: String): EnergyEntity?
}