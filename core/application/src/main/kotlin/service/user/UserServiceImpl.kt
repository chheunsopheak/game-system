package service.user

import constant.SecurityConstant
import entity.user.UserEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import repository.device.DeviceRepository
import repository.user.UserRepository
import request.user.CreateUserRequest
import request.user.LoginRequest
import request.user.UpdateUserRequest
import request.user.UserChangePasswordRequest
import response.user.UserDetailResponse
import response.user.UserResponse
import response.user.UserTokenResponse
import service.token.JwtService
import specification.user.UserFilterSpecification
import wrapper.ApiResult
import wrapper.PaginatedResult
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val deviceRepository: DeviceRepository
) : UserService {
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

    override suspend fun getMe(): ApiResult<UserResponse> {
        val auth = SecurityContextHolder.getContext().authentication
        val principal = auth.principal

        // Assuming CustomUserDetailsService returns UserEntity or implements UserDetails
        val user: UserEntity = when (principal) {
            is UserEntity -> principal
            is UserDetails -> userRepository.findByUserName(principal.username)
                ?: return ApiResult.failed(HttpStatus.NOT_FOUND, "User not found")

            else -> return ApiResult.failed(HttpStatus.UNAUTHORIZED, "Invalid principal type")
        }
        return ApiResult.success(
            UserResponse(
                id = user.id,
                name = user.name,
                email = user.email,
                phone = user.phone,
                photo = user.photo,
                username = user.userName,
                isActive = user.isActive,
                energy = user.energy,
            ),
            "User retrieved successfully"
        )
    }

    override suspend fun getUserInfo(phone: String): ApiResult<UserResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(request: UpdateUserRequest): ApiResult<String> {
        val userId = jwtService.getCurrentUser()
        val user = userRepository.findById(userId)
            ?: return ApiResult.failed(HttpStatus.NOT_FOUND, "User not found")
        val requestUpdate = user.get()

        requestUpdate.name = request.name
        requestUpdate.phone = request.phone
        requestUpdate.photo = request.photo
        requestUpdate.role = request.role
        requestUpdate.lastLogin = LocalDateTime.now()

        val updatedUser = userRepository.save(requestUpdate)
        return ApiResult.success(updatedUser.id, "User updated successfully")
    }

    override suspend fun userRegister(request: CreateUserRequest): ApiResult<UserTokenResponse> {
        if (userRepository.existsByEmail(request.email)) {
            return ApiResult.error(HttpStatus.CONFLICT, "User already exists")
        }
        if (userRepository.existsByPhone(request.phone)) {
            return ApiResult.error(HttpStatus.CONFLICT, "Phone number already exists")
        }
        if (userRepository.findByUserName(request.username) != null) {
            return ApiResult.error(HttpStatus.CONFLICT, "This ${request.username} already taken")
        }
        val newUser = UserEntity(
            userName = request.username,
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            name = request.name,
            photo = request.photo,
            phone = request.phone,
            lastLogin = LocalDateTime.now(),
            energy = 0,
            role = request.role
        )

        val savedUser = userRepository.save(newUser)
        val userToken = jwtService.generateAccessToken(savedUser)

        return ApiResult.success(
            UserTokenResponse(
                userId = savedUser.id,
                accessToken = userToken.token,
                tokenType = "Bearer",
                expiresIn = userToken.expiresIn,
                role = if (savedUser.role == 2) "ADMIN" else "USER"
            ),
            "User created successfully"
        )
    }

    override suspend fun deviceLogin(request: LoginRequest): ApiResult<UserTokenResponse> {
        val user = userRepository.findByUserName(request.username)
            ?: userRepository.findByEmail(request.username)
            ?: userRepository.findByPhone(request.username)
            ?: return ApiResult.failed(HttpStatus.UNAUTHORIZED, "Invalid username or password")

        if (!user.isActive) {
            return ApiResult.failed(HttpStatus.UNAUTHORIZED, "User is not active")
        }
        val userDevice = deviceRepository.findByUserId(user.id)
        if (userDevice == null) {
            return ApiResult.failed(
                HttpStatus.UNAUTHORIZED,
                "Your are no permitted to login without device"
            )
        }

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            return ApiResult.failed(HttpStatus.UNAUTHORIZED, "Invalid username or password")
        }

        user.lastLogin = LocalDateTime.now()
        userRepository.save(user)

        val userToken = jwtService.generateAccessToken(user)

        val response = UserTokenResponse(
            userId = user.id,
            accessToken = userToken.token,
            tokenType = SecurityConstant.TOKEN_PREFIX.trim(),
            expiresIn = userToken.expiresIn,
            role = if (user.role == 2) "ADMIN" else "USER"
        )

        return ApiResult.success(response, "User logged in successfully")
    }

    override suspend fun adminLogin(request: LoginRequest): ApiResult<UserTokenResponse> {
        val user = userRepository.findByUserName(request.username)
            ?: userRepository.findByEmail(request.username)
            ?: userRepository.findByPhone(request.username)
            ?: return ApiResult.failed(HttpStatus.UNAUTHORIZED, "Invalid username or password")

        if (!user.isActive) {
            return ApiResult.failed(HttpStatus.UNAUTHORIZED, "User is not active")
        }

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            return ApiResult.failed(HttpStatus.UNAUTHORIZED, "Invalid username or password")
        }

        user.lastLogin = LocalDateTime.now()
        userRepository.save(user)

        val userToken = jwtService.generateAccessToken(user)

        val response = UserTokenResponse(
            userId = user.id,
            accessToken = userToken.token,
            tokenType = SecurityConstant.TOKEN_PREFIX.trim(),
            expiresIn = userToken.expiresIn,
            role = if (user.role == 2) "ADMIN" else "USER"
        )

        return ApiResult.success(response, "User logged in successfully")
    }

    override suspend fun changePassword(request: UserChangePasswordRequest): ApiResult<String> {
        val userId = jwtService.getCurrentUser()
        val user = userRepository.findById(userId)
            ?: return ApiResult.failed(HttpStatus.NOT_FOUND, "User not found")

        val userRequest = user.get()
        if (!passwordEncoder.matches(request.oldPassword, userRequest.passwordHash)) {
            return ApiResult.failed(HttpStatus.UNAUTHORIZED, "Old password is incorrect")
        }

        userRequest.passwordHash = passwordEncoder.encode(request.newPassword)
        userRepository.save(userRequest)
        return ApiResult.success(null, "Password changed successfully")
    }
}

