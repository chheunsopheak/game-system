package service.auth

import constant.AESConstant.VITE_KEY
import constant.AESConstant.VITE_SECRET
import constant.RetrofitConstant.REWARD_BASE_URL
import network.RetrofitManager
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import request.auth.AuthClientRequest
import request.auth.ClientInfoRequest
import response.auth.AuthClientResponse
import response.auth.ClientInfoResponse
import util.AESUtil
import wrapper.ApiResult

@Service
class AuthClientServiceImpl(retrofitManager: RetrofitManager) : AuthClientService {

    val client = retrofitManager.createClient(REWARD_BASE_URL, AuthClientService::class.java)

    override suspend fun login(request: AuthClientRequest): AuthClientResponse {
        return client.login(request)
    }

    override suspend fun getToken(): ApiResult<String> {
        val requestLogin = AuthClientRequest(
            key = AESUtil.encrypt(VITE_KEY),
            secret = AESUtil.encrypt(VITE_SECRET)
        )

        val data = login(requestLogin)

        if (data.response.status != HttpStatus.OK) {
            return ApiResult.error(
                statusCode = data.response.status.value(),
                message = "Failed to get token: ${data.response.message}"
            )
        }

        println("Token cached: ${data.results}")

        return ApiResult.success(
            data = data.results.token,
            message = "Get token successful"
        )
    }

    override suspend fun getClientByPhone(
        token: String,
        phone: String
    ): ClientInfoResponse {
        val data = client.getClientByPhone("Bearer $token", phone)
        print("data: $data")
        if (data.response.status != HttpStatus.OK) {
            throw Exception("Failed to get user info by phone: $phone")
        }
        return data
    }

    override suspend fun getClientByQr(
        token: String,
        request: ClientInfoRequest
    ): ClientInfoResponse {
        val data = client.getClientByQr("Bearer $token", request)
        print("data: $data")
        if (data.response.status != HttpStatus.OK) {
            throw Exception("Failed to get user info by: $data")
        }
        return data
    }

}