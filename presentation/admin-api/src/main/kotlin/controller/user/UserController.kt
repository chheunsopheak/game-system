package com.gamesystem.controller.user

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import request.auth.ChangePasswordRequest
import request.user.CreateUserRequest
import request.auth.LoginRequest
import request.user.UpdateUserRequest
import request.user.UserChangePasswordRequest
import service.user.UserService
import wrapper.ApiResult


@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class UserController(private val userService: UserService) {
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
    //Change Password
    @PostMapping("user/change-password")
    fun changePassword(@RequestBody request: UserChangePasswordRequest): ResponseEntity<ApiResult<String>> {
        val response = userService.changePassword(request)
        return if (response.statusCode != 200) {
            ResponseEntity.status(response.statusCode).body(response)
        } else {
            ResponseEntity.ok(response)
        }
    }
}