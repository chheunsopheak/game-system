package service.auth

import common.UserRoleHelper
import constant.ApplicationMessage
import constant.RoleConstant
import constant.SecurityConstant
import entity.user.UserEntity
import entity.user.UserTokenEntity
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import repository.token.UserTokenRepository
import repository.user.UserRepository
import request.auth.ChangePasswordRequest
import request.auth.LoginRequest
import request.auth.RefreshTokenRequest
import request.user.CreateUserRequest
import response.user.UserTokenResponse
import service.token.TokenService
import wrapper.ApiResult
import java.time.Duration
import java.time.LocalDateTime

@Service
class AuthServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val tokenService: TokenService,
    private val userRepository: UserRepository,
    private val userTokenRepository: UserTokenRepository,
    private val roleHelper: UserRoleHelper
) : AuthService {
    @Transactional
    override fun adminLogin(request: LoginRequest): ApiResult<UserTokenResponse> {
        val checkUser = userRepository.findByUsername(request.username) ?: userRepository.findByEmail(request.username)
        ?: userRepository.findByPhone(request.username) ?: return ApiResult.failed(
            HttpStatus.BAD_REQUEST.value(), "User not found"
        )

        val user = userRepository.findById(checkUser.id)
        val loginUser = user.get()
        if (!loginUser.isActive) {
            return ApiResult.failed(
                HttpStatus.UNAUTHORIZED.value(), "User is not active"
            )
        }
        val role = roleHelper.getUserRole(loginUser.id).data ?: return ApiResult.error(
            HttpStatus.NOT_FOUND.value(), "User role not found"
        )

        if (role.contains(RoleConstant.ROLE_ADMIN) || role.contains(RoleConstant.ROLE_OPERATOR)) {

            if (!passwordEncoder.matches(request.password, loginUser.passwordHash)) {
                return ApiResult.failed(
                    HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"
                )
            }
            if (passwordEncoder.matches(request.password, loginUser.passwordHash)) {
                val now = LocalDateTime.now()
                loginUser.lastLogin = now
                val refreshExpiry = loginUser.refreshTokenExpiresAt
                if (refreshExpiry == null || Duration.between(now, refreshExpiry).toHours() <= 24) {
                    loginUser.refreshToken = tokenService.generateRefreshToken()
                    loginUser.refreshTokenExpiresAt = now.plusMonths(3)
                }
                val userToken = tokenService.generateToken(loginUser.id)
                val saveToken = saveToken(loginUser.id, userToken.token)

                if (!saveToken) {
                    return ApiResult.failed(
                        HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.INVALID_CREDENTIALS
                    )
                }
                userRepository.save(loginUser)
                val response = UserTokenResponse(
                    userId = loginUser.id,
                    accessToken = userToken.token,
                    tokenType = SecurityConstant.TOKEN_PREFIX.trim(),
                    expiresIn = userToken.expiresIn,
                    role = role,
                    refreshToken = loginUser.refreshToken,
                    refreshTokenExpiresAt = loginUser.refreshTokenExpiresAt
                )

                return ApiResult.success(response, ApplicationMessage.LOGIN_SUCCESS)
            }
        }
        return ApiResult.failed(
            HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.NO_PERMISSION
        )

    }

    @Transactional
    override fun deviceLogin(request: LoginRequest): ApiResult<UserTokenResponse> {
        val user = userRepository.findByUsername(request.username) ?: userRepository.findByEmail(request.username)
        ?: userRepository.findByPhone(request.username) ?: return ApiResult.failed(
            HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"
        )

        if (!user.isActive) {
            return ApiResult.failed(HttpStatus.UNAUTHORIZED.value(), "User is not active")
        }
        val role = roleHelper.getUserRole(user.id).data ?: return ApiResult.error(
            HttpStatus.NOT_FOUND.value(), "User role not found, please contact to support"
        )

        if (role.contains(RoleConstant.ROLE_DEVICE)) {
            if (!passwordEncoder.matches(request.password, user.passwordHash)) {
                return ApiResult.failed(
                    HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.INVALID_PASSWORD
                )
            }

            if (passwordEncoder.matches(request.password, user.passwordHash)) {
                val now = LocalDateTime.now()
                user.lastLogin = now
                val refreshExpiry = user.refreshTokenExpiresAt
                if (refreshExpiry == null || Duration.between(now, refreshExpiry).toHours() <= 24) {
                    user.refreshToken = tokenService.generateRefreshToken()
                    user.refreshTokenExpiresAt = now.plusMonths(3)
                }
                val userToken = tokenService.generateToken(user.id)
                val saveToken = saveToken(user.id, userToken.token)

                if (!saveToken) {
                    return ApiResult.failed(
                        HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.INVALID_CREDENTIALS
                    )
                }
                userRepository.save(user)

                val response = UserTokenResponse(
                    userId = user.id,
                    accessToken = userToken.token,
                    tokenType = SecurityConstant.TOKEN_PREFIX.trim(),
                    expiresIn = userToken.expiresIn,
                    role = role,
                    refreshToken = user.refreshToken,
                    refreshTokenExpiresAt = user.refreshTokenExpiresAt
                )

                return ApiResult.success(response, ApplicationMessage.LOGIN_SUCCESS)
            }
        }

        return ApiResult.failed(
            HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.NO_PERMISSION
        )

    }

    @Transactional
    override fun userLogin(request: LoginRequest): ApiResult<UserTokenResponse> {
        print("data: $request")
        val user = userRepository.findByUsername(request.username) ?: userRepository.findByEmail(request.username)
        ?: userRepository.findByPhone(request.username) ?: return ApiResult.failed(
            HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.USER_NOT_FOUND
        )

        if (!user.isActive) {
            return ApiResult.failed(
                HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.USER_NOT_ACTIVE
            )
        }

        val role = roleHelper.getUserRole(user.id).data ?: return ApiResult.error(
            HttpStatus.NOT_FOUND.value(), "User role not found"
        )

        if (role.contains(RoleConstant.ROLE_USER) || role.contains(RoleConstant.ROLE_GUEST)) {
            if (!passwordEncoder.matches(request.password, user.passwordHash)) {
                return ApiResult.failed(
                    HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.INVALID_PASSWORD
                )
            }

            if (passwordEncoder.matches(request.password, user.passwordHash)) {
                val now = LocalDateTime.now()
                user.lastLogin = now
                val refreshExpiry = user.refreshTokenExpiresAt
                if (refreshExpiry == null || Duration.between(now, refreshExpiry).toHours() <= 24) {
                    user.refreshToken = tokenService.generateRefreshToken()
                    user.refreshTokenExpiresAt = now.plusMonths(3)
                }
                userRepository.save(user)

                val userToken = tokenService.generateToken(user.id)

                val saveToken = saveToken(user.id, userToken.token)
                if (!saveToken) {
                    return ApiResult.failed(
                        HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.INVALID_CREDENTIALS
                    )
                }

                val response = UserTokenResponse(
                    userId = user.id,
                    accessToken = userToken.token,
                    tokenType = SecurityConstant.TOKEN_PREFIX.trim(),
                    expiresIn = userToken.expiresIn,
                    role = role,
                    refreshToken = user.refreshToken,
                    refreshTokenExpiresAt = user.refreshTokenExpiresAt
                )

                return ApiResult.success(response, ApplicationMessage.LOGIN_SUCCESS)
            }
        }

        return ApiResult.failed(
            HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.NO_PERMISSION
        )
    }


    override fun merchantLogin(request: LoginRequest): ApiResult<UserTokenResponse> {
        val user = userRepository.findByUsername(request.username) ?: userRepository.findByEmail(request.username)
        ?: userRepository.findByPhone(request.username) ?: return ApiResult.failed(
            HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.USER_NOT_FOUND
        )

        if (!user.isActive) {
            return ApiResult.failed(
                HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.USER_NOT_ACTIVE
            )
        }

        val role = roleHelper.getUserRole(user.id).data ?: return ApiResult.error(
            HttpStatus.NOT_FOUND.value(), "User role not found"
        )

        if (role.contains(RoleConstant.ROLE_MERCHANT)) {
            if (!passwordEncoder.matches(request.password, user.passwordHash)) {
                return ApiResult.failed(
                    HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.INVALID_PASSWORD
                )
            }

            if (passwordEncoder.matches(request.password, user.passwordHash)) {
                val now = LocalDateTime.now()
                user.lastLogin = now
                val refreshExpiry = user.refreshTokenExpiresAt
                if (refreshExpiry == null || Duration.between(now, refreshExpiry).toHours() <= 24) {
                    user.refreshToken = tokenService.generateRefreshToken()
                    user.refreshTokenExpiresAt = now.plusMonths(3)
                }
                userRepository.save(user)

                val userToken = tokenService.generateToken(user.id)

                val saveToken = saveToken(user.id, userToken.token)
                if (!saveToken) {
                    return ApiResult.failed(
                        HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.INVALID_CREDENTIALS
                    )
                }

                val response = UserTokenResponse(
                    userId = user.id,
                    accessToken = userToken.token,
                    tokenType = SecurityConstant.TOKEN_PREFIX.trim(),
                    expiresIn = userToken.expiresIn,
                    role = role,
                    refreshToken = user.refreshToken,
                    refreshTokenExpiresAt = user.refreshTokenExpiresAt
                )

                return ApiResult.success(response, ApplicationMessage.LOGIN_SUCCESS)
            }
        }
        return ApiResult.failed(
            HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.NO_PERMISSION
        )
    }

    @Transactional
    override fun refresh(request: RefreshTokenRequest): ApiResult<UserTokenResponse> {
        val user = userRepository.findByRefreshToken(request.refreshToken) ?: return ApiResult.failed(
            HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.INVALID_REFRESH_TOKEN
        )

        val now = LocalDateTime.now()
        user.lastLogin = now
        val refreshExpiry = user.refreshTokenExpiresAt
        if (refreshExpiry == null || Duration.between(now, refreshExpiry).toHours() <= 24) {
            user.refreshToken = tokenService.generateRefreshToken()
            user.refreshTokenExpiresAt = now.plusMonths(3)
        }
        val userToken = tokenService.generateToken(user.id)
        userRepository.save(user)

        val saveToken = saveToken(user.id, userToken.token)
        if (!saveToken) {
            return ApiResult.failed(
                HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.INVALID_CREDENTIALS
            )
        }
        val role = roleHelper.getUserRole(user.id).data ?: return ApiResult.error(
            HttpStatus.NOT_FOUND.value(), "User role not found"
        )


        val response = UserTokenResponse(
            userId = user.id,
            accessToken = userToken.token,
            tokenType = SecurityConstant.TOKEN_PREFIX.trim(),
            expiresIn = userToken.expiresIn,
            role = role,
            refreshToken = user.refreshToken,
            refreshTokenExpiresAt = user.refreshTokenExpiresAt
        )
        return ApiResult.success(response, ApplicationMessage.LOGIN_SUCCESS)

    }

    @Transactional
    override fun logout(token: String): ApiResult<String> {
        val userIdOptional = tokenService.getCurrentUserId()

        if (userIdOptional.isEmpty()) {
            return ApiResult.error(
                HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.LOGOUT_FAILED
            )
        }
        val token = userTokenRepository.findByUserId(userIdOptional) ?: return ApiResult.error(
            HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.LOGOUT_FAILED
        )
        userTokenRepository.delete(token)
        return ApiResult.success(
            null, ApplicationMessage.LOGOUT_SUCCESS
        )

    }

    override fun register(request: CreateUserRequest): ApiResult<UserTokenResponse> {
        when {
            userRepository.existsByEmail(request.email) -> return ApiResult.error(
                HttpStatus.CONFLICT.value(), "Email '${request.email}' is already registered"
            )

            userRepository.existsByPhone(request.phone) -> return ApiResult.error(
                HttpStatus.CONFLICT.value(), "Phone number '${request.phone}' is already in use"
            )

            userRepository.findByUsername(request.username) != null -> return ApiResult.error(
                HttpStatus.CONFLICT.value(), "Username '${request.username}' is already taken"
            )
        }
        val newUser = UserEntity(
            username = request.username,
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            name = request.name,
            photoUrl = request.photo,
            phone = request.phone,
            lastLogin = LocalDateTime.now(),
            energy = 0,
            role = request.role,
            coverUrl = request.photo
        )
        newUser.lastLogin = LocalDateTime.now()
        newUser.refreshToken = tokenService.generateRefreshToken()
        newUser.refreshTokenExpiresAt = LocalDateTime.now().plusMonths(3)

        val savedUser = userRepository.save(newUser)
        val userToken = tokenService.generateToken(savedUser.id)
        val saveToken = saveToken(newUser.id, userToken.token)
        if (!saveToken) {
            return ApiResult.error(
                HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.SOMETHING_WENT_WRONG
            )
        }
        val role = roleHelper.getUserRole(savedUser.id).data ?: return ApiResult.error(
            HttpStatus.NOT_FOUND.value(), "User role not found"
        )

        return ApiResult.success(
            UserTokenResponse(
                userId = savedUser.id,
                accessToken = userToken.token,
                tokenType = "Bearer",
                expiresIn = userToken.expiresIn,
                role = role,
                refreshToken = savedUser.refreshToken,
                refreshTokenExpiresAt = savedUser.refreshTokenExpiresAt
            ), "User created successfully"
        )
    }

    override fun forgotPassword(request: LoginRequest): ApiResult<String> {
        TODO("Not yet implemented")
    }

    override fun resetPassword(request: LoginRequest): ApiResult<String> {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun changePassword(request: ChangePasswordRequest): ApiResult<String> {
        val currentUserId = tokenService.getCurrentUserId()

        if (currentUserId.isEmpty()) {
            return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.UNAUTHORIZED)
        }

        val user = userRepository.findById(currentUserId).orElse(null) ?: return ApiResult.error(
            HttpStatus.NOT_FOUND.value(), "User not found"
        )

        if (!passwordEncoder.matches(request.oldPassword, user.passwordHash)) {
            return ApiResult.error(
                HttpStatus.UNAUTHORIZED.value(), ApplicationMessage.PASSWORD_NOT_MATCH
            )
        }
        if (passwordEncoder.matches(request.oldPassword, user.passwordHash)) {
            user.passwordHash = passwordEncoder.encode(request.newPassword)
            userRepository.save(user)

            return ApiResult.success(
                null, ApplicationMessage.PASSWORD_CHANGED
            )
        }
        return ApiResult.error(
            HttpStatus.BAD_REQUEST.value(), ApplicationMessage.SOMETHING_WENT_WRONG
        )

    }

    @Transactional
    private fun saveToken(userId: String, token: String): Boolean {
        val user = userRepository.findById(userId).orElse(null) ?: return false
        val existingToken = userTokenRepository.findByUserId(user.id)

        val userToken = if (existingToken != null) {
            existingToken.token = token
            existingToken
        } else {
            UserTokenEntity(user = user, token = token)
        }

        val result = userTokenRepository.save(userToken)
        return result != null
    }

}