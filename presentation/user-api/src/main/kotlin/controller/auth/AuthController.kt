package com.gamesystem.controller.auth

import constant.BaseUrl
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class AuthController() {

//    //register user
//    @PostMapping("auth/register")
//    fun register(@RequestBody request: CreateUserRequest) = userService.userRegister(request)
//
//    //login user
//    @PostMapping("auth/login")
//    fun login(@RequestBody request: LoginRequest) = userService.deviceLogin(request)
}