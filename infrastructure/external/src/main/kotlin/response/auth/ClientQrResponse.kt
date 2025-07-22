package response.auth

import response.ApiResponse

data class ClientQrResponse(
    val response: ApiResponse,
    val results: ClientQrResult
)