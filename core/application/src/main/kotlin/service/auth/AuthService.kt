package service.auth

import request.auth.ChangePasswordRequest
import request.auth.LoginRequest
import request.auth.RefreshTokenRequest
import request.user.CreateUserRequest
import response.user.UserTokenResponse
import wrapper.ApiResult

interface AuthService {

    fun adminLogin(request: LoginRequest): ApiResult<UserTokenResponse>
    fun deviceLogin(request: LoginRequest): ApiResult<UserTokenResponse>
    fun userLogin(request: LoginRequest): ApiResult<UserTokenResponse>
    fun merchantLogin(request: LoginRequest): ApiResult<UserTokenResponse>
    fun refresh(request: RefreshTokenRequest): ApiResult<UserTokenResponse>
    fun logout(token: String): ApiResult<String>
    fun register(request: CreateUserRequest): ApiResult<UserTokenResponse>
    fun forgotPassword(request: LoginRequest): ApiResult<String>
    fun resetPassword(request: LoginRequest): ApiResult<String>
    fun changePassword(request: ChangePasswordRequest): ApiResult<String>
}