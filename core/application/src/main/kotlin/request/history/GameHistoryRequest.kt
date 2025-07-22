package request.history

data class GameHistoryRequest(
    val userId: String,
    val gameId: String? = null,
    val gameName: String?,
    val device: String,
    val energy: Int
)
