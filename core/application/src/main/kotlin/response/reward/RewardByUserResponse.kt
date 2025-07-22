package response.reward

import entity.reward.UserRewardEntity
import java.time.LocalDateTime

data class RewardByUserResponse(
    val claimLink: String,
    val isClaimed: Boolean,
    val claimAt: LocalDateTime,
) {
    companion object {
        fun from(data: UserRewardEntity): RewardByUserResponse {
            return RewardByUserResponse(
                claimLink = data.rewardUrl,
                isClaimed = data.isClaimed,
                claimAt = data.claimedAt
            )
        }
    }
}
