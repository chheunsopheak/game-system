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
    suspend fun getAllUsers(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<UserResponse>
    suspend fun getUserById(id: String): ApiResult<UserDetailResponse>
    suspend fun getMe(): ApiResult<UserResponse>
    suspend fun getUserInfo(phone: String): ApiResult<UserResponse>
    suspend fun updateUser(request: UpdateUserRequest): ApiResult<String>
    suspend fun userRegister(request: CreateUserRequest): ApiResult<UserTokenResponse>
    suspend fun deviceLogin(request: LoginRequest): ApiResult<UserTokenResponse>
    suspend fun adminLogin(request: LoginRequest): ApiResult<UserTokenResponse>
    suspend fun changePassword(request: UserChangePasswordRequest): ApiResult<String>
}