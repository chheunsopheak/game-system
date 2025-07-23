package service.merchant

import common.UserRole
import constant.SecurityConstant
import entity.energy.EnergyEntity
import entity.merchant.MerchantEntity
import entity.user.UserEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import repository.merchant.MerchantRepository
import repository.store.StoreRepository
import repository.user.UserRepository
import request.merchant.MerchantModeRequest
import request.merchant.MerchantRequest
import request.merchant.MerchantUpdateRequest
import request.user.LoginRequest
import response.merchant.MerchantDetailResponse
import response.merchant.MerchantLoginResponse
import response.merchant.MerchantResponse
import response.merchant.MyMerchantResponse
import service.token.TokenService
import service.user.UserService
import specification.merchant.MerchantFilterSpecification
import wrapper.ApiResult
import wrapper.PaginatedResult
import java.time.LocalDateTime

@Service
class MerchantServiceImpl(
    private val merchantRepository: MerchantRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val storeRepository: StoreRepository,
    private val userService: UserService,
    private val tokenService: TokenService
) : MerchantService {
    override fun merchantLogin(request: LoginRequest): ApiResult<MerchantLoginResponse> {
        val user = userRepository.findByPhone(request.username)
            ?: userRepository.findByEmail(request.username)
            ?: userRepository.findByUsername(request.username)
        if (user == null) {
            return ApiResult.notFound("User not found")
        }

        val merchant = merchantRepository.findByUserId(user.id)
            ?: return ApiResult.notFound("Merchant not found")

        if (!merchant.isActive) {
            return ApiResult.failed(
                HttpStatus.FORBIDDEN.value(),
                "Merchant account is not active"
            )
        }
        val userLogin = userService.adminLogin(request)
        if (!userLogin.success) {
            return ApiResult.error(
                userLogin.statusCode,
                userLogin.message ?: "Login failed"
            )
        }
        if (userLogin.data == null) {
            return ApiResult.error(
                HttpStatus.FORBIDDEN.value(),
                "User is not a merchant"
            )
        }

        val data = MerchantLoginResponse(
            userId = userLogin.data!!.userId,
            merchantId = merchant.id,
            storeId = user.storeId,
            mode = if (user.storeId.isNullOrEmpty()) "MERCHANT" else "STORE",
            accessToken = userLogin.data!!.accessToken,
            expiresIn = userLogin.data!!.expiresIn,
            tokenType = userLogin.data!!.tokenType,
            role = userLogin.data!!.role,
        )

        return ApiResult.success(data, "Merchant login successful")
    }

    override fun getMyMerchant(): ApiResult<MyMerchantResponse> {
        val currentUser = userService.getMe().data
            ?: return ApiResult.notFound("User not found")
        val user = userRepository.findById(currentUser.id)
            ?: return ApiResult.notFound("User not found")
        if (user.get().storeId != null) {
            return ApiResult.error(
                HttpStatus.FORBIDDEN.value(),
                "User is in store mode, not merchant mode"
            )
        }
        val merchant = merchantRepository.findByUserId(user.get().id)
            ?: return ApiResult.notFound("Merchant not found")

        val data = MyMerchantResponse(
            userId = user.get().id,
            userName = user.get().name,
            merchantId = merchant.id,
            merchantName = merchant.name,
            merchantLogo = merchant.logoUrl,
            merchantVerified = merchant.verified,
            merchantEmail = merchant.email,
            merchantPhone = merchant.phoneNumber ?: "",
            merchantAddress = merchant.address ?: "",
            totalTicket = 0//merchantRepository.getTicketCountByMerchantId(merchant.id)
        )
        return ApiResult.success(data, "Merchant retrieved successfully")
    }

    override fun merchantMode(request: MerchantModeRequest): ApiResult<MerchantLoginResponse> {
        if (request.userId.isBlank()) {
            return ApiResult.error(HttpStatus.FORBIDDEN.value(), "User ID must not be empty")
        }

        if (request.mode.isBlank()) {
            return ApiResult.error(HttpStatus.FORBIDDEN.value(), "Mode must not be empty")
        }

        val user = userRepository.findById(request.userId)
            ?: return ApiResult.notFound("User not found")

        val mode = request.mode.uppercase()
        if (mode != "MERCHANT" && mode != "STORE") {
            return ApiResult.error(HttpStatus.BAD_REQUEST.value(), "Invalid mode. Must be either 'MERCHANT' or 'STORE'")
        }

        val merchant = merchantRepository.findByUserId(user.get().id)
            ?: return ApiResult.notFound("Merchant not found")

        if (mode == "MERCHANT") {
            user.get().storeId = null
        } else {
            if (request.storeId.isNullOrBlank()) {
                return ApiResult.badRequest("Target store ID is required when switching to STORE mode")
            }

            val store = storeRepository.findById(request.storeId)
                ?: return ApiResult.notFound("Store not found")

            if (store.get().merchant.id != merchant.id) {
                return ApiResult.error(HttpStatus.FORBIDDEN.value(), "This store does not belong to your merchant")
            }
            user.get().storeId = store.get().id
        }
        val saveUser = userRepository.save(user.get())

        val userToken = tokenService.generateToken(saveUser.id)

        val data = MerchantLoginResponse(
            userId = saveUser.id,
            merchantId = merchant.id,
            storeId = saveUser.storeId,
            mode = mode,
            accessToken = userToken.token,
            tokenType = SecurityConstant.TOKEN_PREFIX.trim(),
            expiresIn = userToken.expiresIn,
            role = mode
        )

        return ApiResult.success(data, "Switched to $mode mode successfully")
    }

    override fun createMerchant(request: MerchantRequest): ApiResult<String> {
        if (request.phone.isBlank()) {
            return ApiResult.badRequest("Phone number must not be empty")
        }
        if (request.email.isBlank()) {
            return ApiResult.badRequest("Email must not be empty")
        }

        if (userRepository.existsByPhone(request.phone)) {
            return ApiResult.error(
                HttpStatus.CONFLICT.value(),
                "Phone number already exists in users"
            )
        }

        if (userRepository.existsByEmail(request.email)) {
            return ApiResult.error(
                HttpStatus.CONFLICT.value(),
                "Email already exists in users"
            )
        }

        val existingMerchantByPhone = merchantRepository.findByPhoneNumber(request.phone)
        if (existingMerchantByPhone != null) {
            return ApiResult.error(
                HttpStatus.CONFLICT.value(),
                "Phone number already exists in merchants"
            )
        }

        val userRequest = UserEntity(
            username = (request.email.takeIf { it.isNotBlank() }?.split("@")?.getOrNull(0)
                ?: request.phone),
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            name = request.name,
            photo = request.logoUrl,
            phone = request.phone,
            lastLogin = LocalDateTime.now(),
            energy = 0,
            role = UserRole.MERCHANT.ordinal
        )

        val userSave = userRepository.save(userRequest)
        val user = userRepository.findById(userSave.id)
            ?: return ApiResult.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to create user for merchant"
            )

        val merchant = MerchantEntity(
            name = request.name,
            email = request.email,
            phoneNumber = request.phone,
            description = request.description,
            logoUrl = request.logoUrl,
            coverUrl = request.coverUrl,
            address = request.address,
            user = user.get()
        )
        merchant.isActive = request.isActive
        merchant.energies.add(
            EnergyEntity(
                merchant = merchant,
                value = 0,
            )
        )
        val saveMerchant = merchantRepository.save(merchant)
        if (saveMerchant.id.isBlank()) {
            return ApiResult.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to create merchant"
            )
        }
        return ApiResult.success(saveMerchant.id, "Merchant has been created successfully")
    }

    override fun updateMerchant(
        id: String,
        request: MerchantUpdateRequest
    ): ApiResult<String> {
        val merchant = merchantRepository.findMerchantById(id)
            ?: return ApiResult.notFound("Merchant not found")

        merchant.name = request.name
        merchant.email = request.email
        merchant.phoneNumber = request.phone
        merchant.logoUrl = request.logoUrl
        merchant.coverUrl = request.coverUrl
        merchant.description = request.description
        merchant.address = request.address
        merchant.isActive = request.isActive

        val updateMerchant = merchantRepository.save(merchant)
//        val merchantEnergy = energyService.getEnergiesByMerchantId(merchant.id)
//        if (merchantEnergy.data != null) {
//            energyService.updateMerchantEnergy(
//                merchantEnergy.data.id,
//                EnergyRequest(request.energy, merchant.id, request.isActive)
//            )
//        }

        return ApiResult.success(updateMerchant.id, "Merchant has been updated successfully")
    }

    override fun getMerchantById(id: String): ApiResult<MerchantDetailResponse> {
        val merchant = merchantRepository.findMerchantById(id)
            ?: return ApiResult.notFound("Merchant not found")

        val data = MerchantDetailResponse(
            id = merchant.id,
            userId = merchant.user.id,
            userName = merchant.name,
            name = merchant.name,
            email = merchant.email,
            phone = merchant.phoneNumber,
            address = merchant.address,
            description = merchant.description,
            logoUrl = merchant.logoUrl,
            coverUrl = merchant.coverUrl,
            energy = merchant.energies[0].value,
            isActive = merchant.isActive,
            createdAt = merchant.createdAt,
            updatedAt = merchant.updatedAt
        )
        return ApiResult.success(data, "Merchant retrieved successfully")
    }

    override fun getAllMerchants(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<MerchantResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = MerchantFilterSpecification.merchantFilterSpecification(searchString)
        val merchantsPage = merchantRepository.findAll(spec, pageable)
        val data = merchantsPage.map(MerchantResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = merchantsPage.totalElements.toInt()
        )
        return response
    }

    override fun deleteMerchant(merchantId: String): ApiResult<String> {
        val merchant = merchantRepository.findMerchantById(merchantId)
            ?: return ApiResult.notFound("Merchant not found")

        val userId = merchant.user.id
        userRepository.findById(userId)
            ?: return ApiResult.failed(HttpStatus.CONFLICT.value(), "Associated user not found")
        try {
            merchantRepository.deleteById(merchantId)
            userRepository.deleteById(userId)
            return ApiResult.success(merchantId, "Merchant and associated user have been removed successfully")
        } catch (ex: Exception) {
            return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete merchant: ${ex.message}")
        }
    }
}