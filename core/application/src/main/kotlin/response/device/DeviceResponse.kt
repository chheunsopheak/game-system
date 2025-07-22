package response.device

import entity.device.DeviceEntity

data class DeviceResponse(
    val id: String,
    val deviceId: String,
    val deviceName: String,
    val deviceType: String,
    val osVersion: String,
    val ipAddress: String,
    val isActive: Boolean?,
) {
    companion object {
        fun from(device: DeviceEntity) = DeviceResponse(
            id = device.id,
            deviceId = device.deviceId,
            deviceName = device.deviceName,
            deviceType = device.deviceType,
            osVersion = device.osVersion,
            ipAddress = device.ipAddress,
            isActive = device.isActive,
        )
    }
}
