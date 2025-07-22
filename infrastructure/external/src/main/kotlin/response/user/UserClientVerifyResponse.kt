package response.user

import response.ApiResponse

data class UserClientVerifyResponse(
    val results: UserClientVerifyResult,
    val response: ApiResponse
)
