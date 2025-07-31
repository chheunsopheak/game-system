package com.gamesystem.controller.auth

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import request.auth.LoginRequest
import request.auth.RefreshTokenRequest
import request.user.CreateUserRequest
import response.user.UserTokenResponse
import service.auth.AuthService
import wrapper.ApiResult


@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class AuthController(private val authService: AuthService) {
    //register user
    @PostMapping("auth/register")
    fun register(@RequestBody request: CreateUserRequest): ResponseEntity<ApiResult<UserTokenResponse>> {
        val response = authService.register(request)
        if (response.statusCode != 200) {
            return ResponseEntity.status(response.statusCode).body(response)
        }
        return ResponseEntity.ok(response)
    }

    //login user
    @PostMapping("auth/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<ApiResult<UserTokenResponse>> {
        val response = authService.adminLogin(request)
        if (response.statusCode != 200) {
            return ResponseEntity.status(response.statusCode).body(response)
        }
        return ResponseEntity.ok(response)
    }

    //refresh token
    @PostMapping("auth/refresh-token")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<ApiResult<UserTokenResponse>> {
        val response = authService.refresh(request)
        if (response.statusCode != 200) {
            return ResponseEntity.status(response.statusCode).body(response)
        }
        return ResponseEntity.ok(response)
    }

    //logout user
    @PostMapping("auth/logout")
    fun logout(@RequestBody token: String): ResponseEntity<ApiResult<String>> {
        val response = authService.logout(token)
        if (response.statusCode != 200) {
            return ResponseEntity.status(response.statusCode).body(response)
        }
        return ResponseEntity.ok(response)
    }

}