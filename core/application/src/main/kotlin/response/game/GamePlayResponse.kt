package response.game

data class GamePlayResponse(
    val rewardUrl: String,
    val rewardStatus: String,
    val photo: String,
    val rewardName: String,
    val ref: String
)
