package response.reward

import entity.reward.UserRewardEntity
import java.time.LocalDateTime

data class RewardHistoryResponse(
    val id: String,
    var name: String,
    val gameIcon: String,
    val gameName: String,
    val rewardName: String,
    val phone: String,
    val rewardImage: String,
    val claimLink: String,
    val deviceId: String,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?
) {
    companion object {
        fun from(userRewardEntity: UserRewardEntity): RewardHistoryResponse {
            return RewardHistoryResponse(
                id = userRewardEntity.id,
                name = userRewardEntity.user.name,
                gameIcon = userRewardEntity.game?.gameIcon.orEmpty(),
                gameName = userRewardEntity.game?.name.orEmpty(),
                rewardName = userRewardEntity.rewardName,
                phone = userRewardEntity.phone,
                rewardImage = userRewardEntity.photo,
                claimLink = userRewardEntity.rewardUrl,
                deviceId = userRewardEntity.deviceId.toString(),
                createdAt = userRewardEntity.createdAt,
                updatedAt = userRewardEntity.updatedAt
            )
        }
    }
}
