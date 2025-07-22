package com.gamesystem.controller.customer

import constant.BaseUrl
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import request.customer.CustomerRegisterRequest
import request.customer.CustomerSendRequest
import request.customer.CustomerVerifyRequest
import service.customer.CustomerService

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class CustomerController(private val customerService: CustomerService) {

    @PostMapping("customer/send-code")
    suspend fun sendCode(@RequestBody request: CustomerSendRequest) = customerService.sendCodeCustomer(request)

    @PostMapping("customer/verify")
    suspend fun verifyCodeCustomer(@RequestBody request: CustomerVerifyRequest) =
        customerService.verifyCodeCustomer(request)

    @PostMapping("customer/register")
    suspend fun registerCustomer(@RequestBody request: CustomerRegisterRequest) =
        customerService.registerCustomer(request)
}