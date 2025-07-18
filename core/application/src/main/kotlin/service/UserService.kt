package service

import request.user.CreateUserRequest
import request.user.UpdateUserRequest
import response.user.UserDetailResponse
import response.user.UserResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface UserService {
    suspend fun getAllUsers(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<UserResponse>
    suspend fun getUserById(id: String): ApiResult<UserDetailResponse>
    suspend fun createUser(request: CreateUserRequest): ApiResult<String>
    suspend fun updateUser(request: UpdateUserRequest): ApiResult<String>
    suspend fun deleteUser(id: String): ApiResult<String>
    suspend fun activateUser(id: String): ApiResult<String>
    suspend fun deactivateUser(id: String): ApiResult<String>
}