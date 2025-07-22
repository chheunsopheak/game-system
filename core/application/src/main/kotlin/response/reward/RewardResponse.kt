package response.reward

data class RewardResponse(
    val name: String,
    val photo: String,
    val description: String,
    val ref: String,
    val merchant: MerchantRewardResponse,
    val startDate: String,
    val endDate: String,
    val weight: Int,
    val item: List<RewardItemResponse>
)
