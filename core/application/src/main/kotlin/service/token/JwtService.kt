package service.token

import entity.user.UserEntity
import io.jsonwebtoken.Claims
import response.TokenResponse

interface JwtService {
    fun generateAccessToken(user: UserEntity): TokenResponse
    fun getClaims(token: String): Claims
    fun getUserName(token: String): String
    fun getUserId(token: String): String
    fun getCurrentUser(): String
    fun isTokenValid(token: String, user: UserEntity): Boolean

}
