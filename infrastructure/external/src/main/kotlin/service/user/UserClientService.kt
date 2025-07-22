package service.user

import request.user.UserClientRegisterRequest
import request.user.UserClientSendRequest
import request.user.UserClientVerifyRequest
import response.auth.ClientQrResponse
import response.user.UserClientResponse
import response.user.UserClientSendResponse
import response.user.UserClientVerifyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserClientService {
    @POST("user/auth/send-code")
    suspend fun sendCode(
        @Header("Authorization") token: String,
        @Body request: UserClientSendRequest
    ): UserClientSendResponse

    @POST("user/auth/verify-code")
    suspend fun verifyCode(
        @Header("Authorization") token: String,
        @Body request: UserClientVerifyRequest
    ): UserClientVerifyResponse

    @POST("user/auth/register")
    suspend fun registerCustomer(
        @Header("Authorization") token: String,
        @Body request: UserClientRegisterRequest
    ): UserClientResponse

    @GET("user/qr")
    suspend fun getUserByQr(
        @Header("Authorization") token: String,
    ): ClientQrResponse
}