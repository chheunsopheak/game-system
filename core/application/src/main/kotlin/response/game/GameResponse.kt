package response.game

import entity.game.GameEntity

data class GameResponse(
    var id: String,
    var name: String,
    var gameIcon: String,
    var gameUrl: String,
    var energy: Int,
    val threshold: Int,
    var description: String,
    var category: String,
    var isActive: Boolean = true
) {
    companion object {
        fun from(game: GameEntity): GameResponse = GameResponse(
            id = game.id,
            name = game.name,
            gameIcon = game.gameIcon,
            gameUrl = game.gameUrl,
            energy = game.energy,
            threshold = game.threshold,
            description = game.description,
            category = game.category,
            isActive = game.isActive
        )
    }
}
