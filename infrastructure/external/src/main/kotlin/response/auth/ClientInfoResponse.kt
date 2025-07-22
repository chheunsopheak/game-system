package response.auth

import response.ApiResponse

data class ClientInfoResponse(
    val results: ClientResultResponse,
    val response: ApiResponse
)