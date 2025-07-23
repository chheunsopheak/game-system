package com.gamesystem.controller.customer

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import request.customer.CustomerRegisterRequest
import request.customer.CustomerSendRequest
import request.customer.CustomerVerifyRequest
import response.customer.CustomerTokenResponse
import response.customer.CustomerVerifyResponse
import service.customer.CustomerService
import wrapper.ApiResult

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class CustomerController(private val customerService: CustomerService) {

    @PostMapping("customer/send-code")
    suspend fun sendCode(@RequestBody request: CustomerSendRequest): ResponseEntity<ApiResult<String>> {
        val result = customerService.sendCodeCustomer(request)
        return ResponseEntity.status(result.statusCode).body(result)
    }

    @PostMapping("customer/verify")
    suspend fun verifyCodeCustomer(@RequestBody request: CustomerVerifyRequest)
            : ResponseEntity<ApiResult<CustomerVerifyResponse>> {
        val result = customerService.verifyCodeCustomer(request)
        return ResponseEntity.status(result.statusCode).body(result)
    }

    @PostMapping("customer/register")
    suspend fun registerCustomer(@RequestBody request: CustomerRegisterRequest)
            : ResponseEntity<ApiResult<CustomerTokenResponse>> {
        val result = customerService.registerCustomer(request)
        return ResponseEntity.status(result.statusCode).body(result)
    }

}