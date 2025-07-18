package service

import entity.user.UserEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.user.UserRepository
import request.user.CreateUserRequest
import request.user.UpdateUserRequest
import response.user.UserDetailResponse
import response.user.UserResponse
import specification.user.UserFilterSpecification
import wrapper.ApiResult
import wrapper.PaginatedResult

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override suspend fun getAllUsers(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
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

    override suspend fun getUserById(id: String): ApiResult<UserDetailResponse> {
        val user = userRepository.findById(id)
        if (user.isEmpty)
            return ApiResult.failed(HttpStatus.NOT_EXTENDED, "User with id $id not found")
        val data = UserDetailResponse.from(user.get())
        return ApiResult.success(data, "User retrieved successfully")
    }

    override suspend fun createUser(request: CreateUserRequest): ApiResult<String> {
        if (userRepository.existsByUsername(request.username)) {
            return ApiResult.failed(HttpStatus.CONFLICT, "This ${request.username} already exists")
        }
        if (userRepository.existsByEmail(request.email)) {
            return ApiResult.failed(HttpStatus.CONFLICT, "Email ${request.email} already exists")
        }

        val requestSaved = UserEntity(
            username = request.username,
            email = request.email,
            name = request.name,
            password = request.password,
        )
        requestSaved.isActive = request.isActive
        val savedUser = userRepository.save(requestSaved)
        if (savedUser.id.isEmpty()) {
            return ApiResult.failed(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user")
        }
        return ApiResult.success(savedUser.id, "User created successfully")
    }

    override suspend fun updateUser(request: UpdateUserRequest): ApiResult<String> {
        val user = userRepository.findById(request.id)
        if (user.isEmpty) {
            return ApiResult.failed(HttpStatus.NOT_FOUND, "User with id ${request.id} not found")
        }

        val existingUser = user.get()
        if (request.username != null && request.username != existingUser.username &&
            userRepository.existsByUsername(request.username)
        ) {
            return ApiResult.failed(HttpStatus.CONFLICT, "Username ${request.username} already exists")
        }
        if (request.email != null && request.email != existingUser.email &&
            userRepository.existsByEmail(request.email)
        ) {
            return ApiResult.failed(HttpStatus.CONFLICT, "Email ${request.email} already exists")
        }

        existingUser.username = request.username ?: existingUser.username
        existingUser.email = request.email ?: existingUser.email
        existingUser.name = request.name ?: existingUser.name
        existingUser.isActive = request.isActive

        val updatedUser = userRepository.save(existingUser)
        if (updatedUser.id.isEmpty()) {
            return ApiResult.failed(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user")
        }

        return ApiResult.success(updatedUser.id, "User updated successfully")
    }

    override suspend fun deleteUser(id: String): ApiResult<String> {
        val user = userRepository.findById(id)
        if (user.isEmpty) {
            return ApiResult.failed(HttpStatus.NOT_FOUND, "User with id $id not found")
        }
        userRepository.delete(user.get())
        return ApiResult.success(null, "User with id $id deleted successfully")
    }

    override suspend fun activateUser(id: String): ApiResult<String> {
        val user = userRepository.findById(id)
        if (user == null) {
            return ApiResult.failed(HttpStatus.NOT_FOUND, "User with id $id not found")
        }
        val updatedUser = user.get()
        updatedUser.isActive = true
        val saved = userRepository.save(updatedUser)
        return ApiResult.success(saved.id, "User with id $id activated successfully")
    }

    override suspend fun deactivateUser(id: String): ApiResult<String> {
        val user = userRepository.findById(id)
        if (user == null) {
            return ApiResult.failed(HttpStatus.NOT_FOUND, "User with id $id not found")
        }
        val updatedUser = user.get()
        updatedUser.isActive = false
        val saved = userRepository.save(updatedUser)
        return ApiResult.success(saved.id, "User with id $id deactivated successfully")
    }


}

