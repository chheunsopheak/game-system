package request.device

data class DeviceRequest(
    val password: String,
    val photo: String?,
    val deviceId: String,
    val deviceName: String,
    val deviceType: String,
    val osVersion: String,
    val ipAddress: String
)
