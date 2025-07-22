package service.auth

import request.auth.AuthClientRequest
import request.auth.ClientInfoRequest
import response.auth.AuthClientResponse
import response.auth.ClientInfoResponse
import retrofit2.http.*
import wrapper.ApiResult

interface AuthClientService {
    @POST("partner/v1/login")
    suspend fun login(@Body request: AuthClientRequest): AuthClientResponse
    suspend fun getToken(): ApiResult<String>

    @GET("partner/v1/customer-info/{phone}")
    suspend fun getClientByPhone(
        @Header("Authorization") token: String,
        @Path("phone") phone: String
    ): ClientInfoResponse

    @POST("partner/v1/customer-info/qr")
    suspend fun getClientByQr(
        @Header("Authorization") token: String,
        @Body request: ClientInfoRequest
    ): ClientInfoResponse
}