package service.user

import constant.RetrofitConstant.REGISTER_BASE_URL
import network.RetrofitManager
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import request.user.UserClientRegisterRequest
import request.user.UserClientSendRequest
import request.user.UserClientVerifyRequest
import response.auth.ClientQrResponse
import response.user.UserClientResponse
import response.user.UserClientSendResponse
import response.user.UserClientVerifyResponse

@Service
class UserClientServiceImpl(retrofit: RetrofitManager) : UserClientService {
    val client = retrofit.createClient(REGISTER_BASE_URL, UserClientService::class.java)
    override suspend fun sendCode(
        token: String,
        request: UserClientSendRequest
    ): UserClientSendResponse {
        println("Request to send code: $request")
        println("Token: $token")
        val data = client.sendCode("Bearer $token", request)
        println("data: $data")

        if (data.response.status != HttpStatus.OK) {
            throw Exception("Failed to send code: ${data.response.message}")
        }
        return data
    }

    override suspend fun verifyCode(
        token: String,
        request: UserClientVerifyRequest
    ): UserClientVerifyResponse {
        val data = client.verifyCode("Bearer $token", request)
        println("data: $data")
        if (data.response.status != HttpStatus.OK) {
            throw Exception("Failed to verify code: ${data.response.message}")
        }
        return data
    }

    override suspend fun registerCustomer(
        token: String,
        request: UserClientRegisterRequest
    ): UserClientResponse {
        val data = client.registerCustomer("Bearer $token", request)
        println("data: $data")
        if (data.response.status != HttpStatus.OK) {
            throw Exception("Failed to register customer: ${data.response.message}")
        }
        return data
    }

    override suspend fun getUserByQr(token: String): ClientQrResponse {
        val data = client.getUserByQr("Bearer $token")
        println("data: $data")
        if (data.response.status != HttpStatus.OK) {
            throw Exception("Failed to get user by qr: ${data.response.message}")
        }
        return data
    }
}