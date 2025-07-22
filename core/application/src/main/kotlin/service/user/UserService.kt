package service.user

import request.user.CreateUserRequest
import request.user.LoginRequest
import request.user.UpdateUserRequest
import request.user.UserChangePasswordRequest
import response.user.UserDetailResponse
import response.user.UserResponse
import response.user.UserTokenResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface UserService {
    fun getAllUsers(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<UserResponse>
    fun getUserById(id: String): ApiResult<UserDetailResponse>
    fun getMe(): ApiResult<UserResponse>
    fun getUserInfo(phone: String): ApiResult<UserResponse>
    fun updateUser(request: UpdateUserRequest): ApiResult<String>
    fun userRegister(request: CreateUserRequest): ApiResult<UserTokenResponse>
    fun deviceLogin(request: LoginRequest): ApiResult<UserTokenResponse>
    fun adminLogin(request: LoginRequest): ApiResult<UserTokenResponse>
    fun changePassword(request: UserChangePasswordRequest): ApiResult<String>
}