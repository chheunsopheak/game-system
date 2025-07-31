package service.customer

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import request.customer.CustomerRegisterRequest
import request.customer.CustomerSendRequest
import request.customer.CustomerVerifyRequest
import request.user.CreateUserRequest
import request.user.UserClientRegisterRequest
import request.user.UserClientSendRequest
import request.user.UserClientVerifyRequest
import response.customer.CustomerTokenResponse
import response.customer.CustomerVerifyResponse
import service.auth.AuthClientService
import service.auth.AuthService
import service.user.UserClientService
import wrapper.ApiResult

@Service
class CustomerServiceImpl(
    private val userClientService: UserClientService,
    private val authClient: AuthClientService,
    private val authService: AuthService
) : CustomerService {
    override suspend fun sendCodeCustomer(request: CustomerSendRequest): ApiResult<String> {
        val token = authClient.getToken()
        if (token.statusCode != HttpStatus.OK.value()) {
            return ApiResult.error(
                statusCode = token.statusCode, message = "Failed to get token: ${token.message}"
            )
        }
        val data = userClientService.sendCode(
            token.data.toString(), UserClientSendRequest(phone = request.phone)
        )
        return if (data.response.status.value() == HttpStatus.OK.value()) {
            ApiResult.success("", data.response.message)
        } else {
            ApiResult.failed(HttpStatus.FAILED_DEPENDENCY.value(), data.response.message)
        }
    }

    override suspend fun verifyCodeCustomer(request: CustomerVerifyRequest): ApiResult<CustomerVerifyResponse> {
        val token = authClient.getToken()
        if (token.statusCode != HttpStatus.OK.value()) {
            return ApiResult.error(
                statusCode = token.statusCode, message = "Failed to get token: ${token.message}"
            )
        }
        val requestVerify = UserClientVerifyRequest(code = request.code, phone = request.phone)
        val data = userClientService.verifyCode(token.data.toString(), requestVerify)
        return if (data.response.status.value() == HttpStatus.OK.value()) {
            val result = CustomerVerifyResponse(ref = data.results.ref)
            ApiResult.success(result, data.response.message)
        } else {
            ApiResult.failed(HttpStatus.FAILED_DEPENDENCY.value(), data.response.message)
        }
    }

    override suspend fun registerCustomer(request: CustomerRegisterRequest): ApiResult<CustomerTokenResponse> {
        val token = authClient.getToken()

        val requestRegister = UserClientRegisterRequest(
            ref = request.ref,
            gender = request.gender,
            name = request.name,
            dob = request.dob,
            nid = request.nid,
            passcode = request.passcode
        )
        val data = userClientService.registerCustomer(token.data!!, requestRegister)
        if (data.response.status.value() == HttpStatus.OK.value()) {
            val userLocalRequest = CreateUserRequest(
                phone = request.phone,
                name = request.name,
                username = request.phone,
                email = "",
                password = request.passcode,
                photo = "",
                role = 1,
            )
            val user = authService.register(userLocalRequest)
            if (user.statusCode != HttpStatus.OK.value()) {
                return ApiResult.failed(user.statusCode, user.message)
            }
            if (user.data == null) {
                return ApiResult.failed(HttpStatus.FAILED_DEPENDENCY.value(), user.message)
            }

            val userQrResult = userClientService.getUserByQr(data.results.token)
            if (userQrResult.response.status.value() != HttpStatus.OK.value()) {
                return ApiResult.failed(userQrResult.response.status.value(), userQrResult.response.message)
            }
            val dataResult = CustomerTokenResponse(
                userId = user.data?.userId!!,
                accessToken = user.data!!.accessToken,
                tokenType = user.data!!.tokenType,
                expiresIn = user.data!!.expiresIn,
                role = user.data!!.role,
                userQr = userQrResult.results.data,
                refreshToken = user.data!!.refreshToken
            )
            return ApiResult.success(dataResult, data.response.message)
        } else {
            return ApiResult.failed(HttpStatus.FAILED_DEPENDENCY.value(), data.response.message)
        }
    }


}