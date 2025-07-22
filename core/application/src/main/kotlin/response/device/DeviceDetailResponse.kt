package response.device

import entity.device.DeviceEntity
import java.time.LocalDateTime

data class DeviceDetailResponse(
    val id: String,
    val deviceId: String,
    val deviceName: String,
    val deviceType: String,
    val osVersion: String,
    val ipAddress: String,
    val lastOnlineAt: LocalDateTime,
    val isActive: Boolean?,
    val registeredAt: LocalDateTime?,
) {
    companion object {
        fun from(device: DeviceEntity) = DeviceDetailResponse(
            id = device.id,
            deviceId = device.deviceId,
            deviceName = device.deviceName,
            deviceType = device.deviceType,
            osVersion = device.osVersion,
            ipAddress = device.ipAddress,
            lastOnlineAt = device.lastOnlineAt,
            isActive = device.isActive,
            registeredAt = device.registeredAt
        )
    }

}

