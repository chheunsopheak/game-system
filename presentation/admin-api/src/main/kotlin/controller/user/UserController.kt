package com.gamesystem.controller.user

import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import request.user.CreateUserRequest
import request.user.LoginRequest
import request.user.UpdateUserRequest
import service.user.UserService


@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class UserController(private val userService: UserService) {
    //register user
    @PostMapping("auth/register")
    fun register(@RequestBody request: CreateUserRequest) = userService.userRegister(request)

    //login user
    @PostMapping("auth/login")
    fun login(@RequestBody request: LoginRequest) = userService.adminLogin(request)

    //get me
    @GetMapping("me")
    fun getMe() = userService.getMe()

    //update user
    @PutMapping("user")
    fun updateUser(@RequestBody request: UpdateUserRequest) = userService.updateUser(request)

    //get all users
    @GetMapping("users")
    fun getAllUser(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam("searchString") searchString: String?
    ) = userService.getAllUsers(pageNumber, pageSize, searchString)

//    //top up energy
//    @PostMapping("user-energy/top-up")
//    fun topUpEnergy(@RequestBody request: UserTopUpEnergyRequest): ApiResult<String> {
//        return userService.topUpUserEnergy(request)
//    }
}