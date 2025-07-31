package service.user

import common.UserRoleHelper
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import repository.device.DeviceRepository
import repository.user.UserRepository
import request.user.UpdateUserRequest
import request.user.UserChangePasswordRequest
import response.user.UserDetailResponse
import response.user.UserResponse
import service.token.TokenService
import specification.user.UserFilterSpecification
import wrapper.ApiResult
import wrapper.PaginatedResult
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenService: TokenService,
    private val deviceRepository: DeviceRepository,
) : UserService {
    override fun getAllUsers(
        pageNumber: Int, pageSize: Int, searchString: String?
    ): PaginatedResult<UserResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = UserFilterSpecification.userFilterSpecification(searchString)
        val usersPage = userRepository.findAll(spec, pageable)
        val data = usersPage.map(UserResponse::from)

        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = usersPage.totalElements.toInt()
        )
        return response
    }

    override fun getUserById(id: String): ApiResult<UserDetailResponse> {
        val user = userRepository.findById(id)
        if (user.isEmpty) return ApiResult.failed(HttpStatus.NOT_EXTENDED.value(), "User with id $id not found")
        val data = UserDetailResponse.from(user.get())
        return ApiResult.success(data, "User retrieved successfully")
    }

    override fun getMe(): ApiResult<UserResponse> {
        val userId = tokenService.getCurrentUserId()
        val user =
            userRepository.findById(userId) ?: return ApiResult.failed(HttpStatus.NOT_FOUND.value(), "User not found")
        val userData = user.get()
        return ApiResult.success(
            UserResponse(
                id = userData.id,
                name = userData.name,
                email = userData.email,
                phone = userData.phone,
                photo = userData.photoUrl,
                username = userData.username,
                isActive = userData.isActive,
                energy = userData.energy,
            ), "User retrieved successfully"
        )
    }

    override fun getUserInfo(phone: String): ApiResult<UserResponse> {
        TODO("Not yet implemented")
    }

    override fun updateUser(request: UpdateUserRequest): ApiResult<String> {
        val userId = tokenService.getCurrentUserId()
        val user =
            userRepository.findById(userId) ?: return ApiResult.failed(HttpStatus.NOT_FOUND.value(), "User not found")
        val requestUpdate = user.get()

        requestUpdate.name = request.name
        requestUpdate.phone = request.phone
        requestUpdate.photoUrl = request.photo
        requestUpdate.role = request.role
        requestUpdate.lastLogin = LocalDateTime.now()

        val updatedUser = userRepository.save(requestUpdate)
        return ApiResult.success(updatedUser.id, "User updated successfully")
    }

    override fun changePassword(request: UserChangePasswordRequest): ApiResult<String> {
        val userId = tokenService.getCurrentUserId()
        val user =
            userRepository.findById(userId) ?: return ApiResult.failed(HttpStatus.NOT_FOUND.value(), "User not found")

        val userRequest = user.get()
        if (!passwordEncoder.matches(request.oldPassword, userRequest.passwordHash)) {
            return ApiResult.failed(HttpStatus.UNAUTHORIZED.value(), "Old password is incorrect")
        }

        userRequest.passwordHash = passwordEncoder.encode(request.newPassword)
        userRepository.save(userRequest)
        return ApiResult.success(null, "Password changed successfully")
    }
}

