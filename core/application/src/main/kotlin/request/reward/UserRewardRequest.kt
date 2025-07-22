package request.reward

data class UserRewardRequest(
    val phone: String,
    val gameId: String?,
    val gameName: String?,
    val energy: Int,
    val deviceId: String,
    val photo: String,
    val rewardName: String,
    val rewardUrl: String,
    val merchantName: String,
    val description: String,
    val ref: String
)
