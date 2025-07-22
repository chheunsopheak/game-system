package request.game

data class GamePlayRequest(
    val deviceName: String,
    val energy: Int,
    val phone: String,
    val gameName: String? = null,
)
