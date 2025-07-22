package request.game

data class GameRequest(
    var name: String,
    var gameIcon: String,
    var gameUrl: String,
    var energy: Int,
    val threshold: Int,
    var description: String,
    var category: String,
    var isActive: Boolean = true
)
