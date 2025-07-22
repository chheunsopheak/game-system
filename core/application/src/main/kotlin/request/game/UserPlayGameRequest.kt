package request.game

data class UserPlayGameRequest(
    val deviceName: String,
    val gameId: String?,
    val gameName: String?,
    val energy: Int,
    val phone: String
)