package response.reward

import entity.reward.UserRewardEntity
import java.time.LocalDateTime

data class RewardDetailResponse(
    val id: String,
    val rewardName: String,
    val rewardUrl: String,
    val rewardPhoto: String,
    val userName: String,
    val userPhoto: String?,
    val phone: String,
    val merchantName: String,
    val ref: String,
    val description: String,
    val gameName: String,
    val gameIcon: String,
    val deviceId: String?,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) {
    companion object {
        fun from(reward: UserRewardEntity): RewardDetailResponse = RewardDetailResponse(
            id = reward.id,
            rewardName = reward.rewardName,
            rewardUrl = reward.rewardUrl,
            rewardPhoto = reward.photo,
            userName = reward.user.name,
            userPhoto = reward.user.photoUrl,
            phone = reward.user.phone.orEmpty(),
            merchantName = reward.merchantName,
            ref = reward.ref,
            description = reward.description,
            gameName = reward.game?.name.orEmpty(),
            gameIcon = reward.game?.gameIcon.orEmpty(),
            deviceId = reward.deviceId,
            createdAt = reward.createdAt,
            updatedAt = reward.updatedAt
        )
    }
}
