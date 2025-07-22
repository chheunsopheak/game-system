package response.reward

import response.ApiResponse

data class RewardClientResponse(
    val results: List<RewardClientResult>,
    val response: ApiResponse
)
