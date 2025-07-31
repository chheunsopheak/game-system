package response.history

import entity.history.GameHistoryEntity
import java.time.LocalDateTime

data class UserGameHistoryResponse(
    val id: String,
    val gameId: String,
    val gameName: String,
    val gameIcon: String,
    val userId: String,
    val userName: String,
    val userPhoto: String?,
    val deviceId: String,
    val energy: Int,
    val playedAt: LocalDateTime
) {
    companion object {
        fun from(entity: GameHistoryEntity): UserGameHistoryResponse {
            return UserGameHistoryResponse(
                id = entity.id,
                gameId = entity.game!!.id,
                gameName = entity.game?.name ?: entity.gameName,
                gameIcon = entity.game!!.gameIcon,
                userId = entity.user!!.id,
                userName = entity.user!!.name,
                userPhoto = entity.user!!.photoUrl,
                deviceId = entity.device,
                energy = entity.energy,
                playedAt = entity.playedAt
            )
        }
    }
}
