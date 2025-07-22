package response.reward

data class RewardClientResult(
    val name: String,
    val photo: String,
    val ref: String,
    val description: String,
    val merchant: MerchantClientResponse,
    val startDate: String,
    val endDate: String,
    val item: List<RewardItemResponse>
)