package repository.device

import entity.device.DeviceEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface DeviceRepository : BaseRepository<DeviceEntity, String> {
    fun findByUserId(userId: String): DeviceEntity?

}