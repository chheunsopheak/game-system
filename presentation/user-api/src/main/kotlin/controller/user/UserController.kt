package com.gamesystem.controller.user

import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import request.user.CreateUserRequest
import request.user.LoginRequest
import service.user.UserService

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class UserController(private val userService: UserService) {

    //register user
    @PostMapping("auth/register")
    suspend fun register(@RequestBody request: CreateUserRequest) = userService.userRegister(request)

    //login user
    @PostMapping("auth/login")
    suspend fun login(@RequestBody request: LoginRequest) = userService.deviceLogin(request)

    //get me
    @GetMapping("me")
    suspend fun getMe() = userService.getMe()

    //user info
    @GetMapping("user-info")
    suspend fun getUserInfo(phone: String) = userService.getUserInfo(phone)

//    //user info
//    @PostMapping("customer-info")
//    suspend fun getUserInfo(@RequestBody request: UserInfoRequest): ApiResult<UserResponse> {
//        return userService.getCustomerInfo(request)
//    }

//    //top up energy
//    @PutMapping("user/top-up")
//    suspend fun topUpEnergy(@RequestBody request: UserTopUpEnergyRequest): ApiResult<String> {
//        return userService.topUpUserEnergy(request)
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