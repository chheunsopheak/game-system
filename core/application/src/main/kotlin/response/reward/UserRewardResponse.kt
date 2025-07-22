package response.reward

import entity.reward.UserRewardEntity
import java.time.LocalDateTime

data class UserRewardResponse(
    val id: String,
    val rewardName: String,
    val phone: String,
    val rewardImage: String,
    val rewardLink: String,
    val gameName: String,
    val gameIcon: String,
    val createDate: LocalDateTime?,
) {
    companion object {
        fun from(userRewardEntity: UserRewardEntity): UserRewardResponse {
            return UserRewardResponse(
                id = userRewardEntity.id,
                rewardName = userRewardEntity.rewardName,
                phone = userRewardEntity.phone,
                rewardImage = userRewardEntity.photo,
                rewardLink = userRewardEntity.rewardUrl,
                gameName = userRewardEntity.gameName.orEmpty(),
                gameIcon = "",
                createDate = userRewardEntity.createdAt
            )
        }
    }
}
