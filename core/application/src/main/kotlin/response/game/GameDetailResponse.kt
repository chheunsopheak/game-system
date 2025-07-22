package response.game

import entity.game.GameEntity
import java.time.LocalDateTime

data class GameDetailResponse(
    var id: String,
    var name: String,
    var gameIcon: String,
    var gameUrl: String,
    var energy: Int,
    val threshold: Int,
    var description: String,
    var category: String,
    var isActive: Boolean = true,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime?
) {
    companion object {
        fun from(game: GameEntity): GameDetailResponse = GameDetailResponse(
            id = game.id,
            name = game.name,
            gameIcon = game.gameIcon,
            gameUrl = game.gameUrl,
            energy = game.energy,
            threshold = game.threshold,
            description = game.description,
            category = game.category,
            isActive = game.isActive,
            createdAt = game.createdAt,
            updatedAt = game.updatedAt
        )
    }
}
