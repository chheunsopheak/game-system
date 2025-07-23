package com.gamesystem.controller.user

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import response.user.UserResponse
import service.customer.CustomerService
import service.user.UserService
import wrapper.ApiResult

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class UserController(
    private val userService: UserService,
    private val customerService: CustomerService
) {
    //get me
    @GetMapping("me")
    fun getMe(): ResponseEntity<ApiResult<UserResponse>> {
        val response = userService.getMe()
        return ResponseEntity.status(response.statusCode).body(response)
    }

    //user info
    @GetMapping("user-info")
    fun getUserInfo(phone: String): ResponseEntity<ApiResult<UserResponse>> {
        val response = userService.getUserInfo(phone)
        return ResponseEntity.status(response.statusCode).body(response)
    }

    //user info
//    @PostMapping("customer-info")
//    suspend fun getUserInfo(@RequestBody request: UserInfoRequest): ApiResult<UserResponse> {
//        return customerService.getCustomerInfo(request)
//    }
//
//    //top up energy
//    @PutMapping("user/top-up")
//    suspend fun topUpEnergy(@RequestBody request: UserTopUpEnergyRequest): ApiResult<String> {
//        return customerService.topUpUserEnergy(request)
//    }
//
//    @PutMapping("user/deduct-energy")
//    suspend fun userDeductEnergy(
//        @RequestParam userId: String,
//        @RequestParam energy: Int
//    ): ApiResult<String> {
//        return userService.userDeductEnergy(userId, energy)
//    }
}