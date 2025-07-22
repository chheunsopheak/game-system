package response.auth

import response.ApiResponse

data class AuthClientResponse(
    val results: AuthResult,
    val response: ApiResponse
)
