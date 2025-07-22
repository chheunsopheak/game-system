package request.energy

data class EnergyLogRequest(
    var merchantId: String,
    var userId: String,
    var value: Int,
    val note: String
)
