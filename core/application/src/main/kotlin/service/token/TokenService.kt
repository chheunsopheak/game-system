package service.token

import response.TokenResponse

interface TokenService {
    fun generateToken(userId: String): TokenResponse
    fun generateRefreshToken(): String
    fun isTokenValid(token: String): Boolean
    fun getCurrentUserId(): String
    fun getUserId(token: String): String?
    fun getUserName(): String
    fun getToken(): String
}