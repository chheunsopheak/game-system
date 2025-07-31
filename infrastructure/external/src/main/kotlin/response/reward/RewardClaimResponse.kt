package response.reward

import response.ApiResponse

data class RewardClaimResponse(
    val results: RewardClaimResult,
    val response: ApiResponse
)