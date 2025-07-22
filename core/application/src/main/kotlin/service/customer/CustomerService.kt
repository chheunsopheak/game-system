package service.customer

import request.customer.CustomerRegisterRequest
import request.customer.CustomerSendRequest
import request.customer.CustomerVerifyRequest
import response.customer.CustomerTokenResponse
import response.customer.CustomerVerifyResponse
import wrapper.ApiResult

interface CustomerService {
    suspend fun sendCodeCustomer(request: CustomerSendRequest): ApiResult<String>
    suspend fun verifyCodeCustomer(request: CustomerVerifyRequest): ApiResult<CustomerVerifyResponse>
    suspend fun registerCustomer(request: CustomerRegisterRequest): ApiResult<CustomerTokenResponse>
}