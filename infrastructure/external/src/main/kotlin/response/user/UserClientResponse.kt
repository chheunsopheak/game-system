package response.user

import response.ApiResponse

data class UserClientResponse(
    val results: UserClientResult,
    val response: ApiResponse
)
