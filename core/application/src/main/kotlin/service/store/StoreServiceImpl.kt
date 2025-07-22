package service.store

import entity.store.StoreEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.merchant.MerchantRepository
import repository.store.StoreRepository
import repository.user.UserRepository
import request.store.StoreRequest
import response.store.MyStoreResponse
import response.store.StoreDetailResponse
import response.store.StoreResponse
import service.user.UserService
import specification.store.StoreFilterSpecification
import wrapper.ApiResult
import wrapper.PaginatedResult

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository,
    private val merchantRepository: MerchantRepository,
    private val userService: UserService
) : StoreService {
    override suspend fun myStore(storeId: String): ApiResult<MyStoreResponse> {
        if (storeId.isBlank()) {
            return ApiResult.failed(
                HttpStatus.BAD_REQUEST,
                "Store ID cannot be empty"
            )
        }

        val currentUser = userService.getMe().data
            ?: return ApiResult.failed(
                HttpStatus.UNAUTHORIZED,
                "User not authenticated"
            )
        val user = userRepository.findById(currentUser.id)
            ?: return ApiResult.notFound("User with ID ${currentUser.id} does not exist")
        if (user.get().storeId.isNullOrEmpty()) {
            return ApiResult.error(
                HttpStatus.FORBIDDEN,
                "User is in merchant mode, not store mode"
            )
        }

        val merchant = merchantRepository.findByUserId(user.get().id)
            ?: return ApiResult.notFound("Merchant not found for user ${user.get().id}")
        val store = storeRepository.findById(storeId)
            ?: return ApiResult.notFound("Store with ID $storeId does not exist")
        if (store.get().merchant.id != merchant.id) {
            return ApiResult.failed(
                HttpStatus.FORBIDDEN,
                "Store with ID $storeId does not belong to the merchant"
            )
        }
        val data = MyStoreResponse(
            userId = user.get().id,
            merchantId = store.get().merchant.id,
            merchantName = store.get().merchant.name,
            location = store.get().location,
            contactNumber = store.get().contactNumber,
            storeId = store.get().id,
            storeName = store.get().name,
            storeLogo = null,
            totalTicket = 0//merchantRepository.getTicketCountByMerchantId(merchant.id)
        )
        return ApiResult.success(
            data = data,
            message = "Stores retrieved successfully"
        )
    }

    override suspend fun createStore(request: StoreRequest): ApiResult<String> {
        if (request.name.isBlank() || request.merchantId.isBlank()) {
            return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Store name and merchant ID cannot be empty"
            )
        }
        if (storeRepository.existsByNameAndMerchantId(request.name, request.merchantId)) {
            return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Store with this merchant ID already exists"
            )
        }
        val merchant = merchantRepository.findMerchantById(request.merchantId)
        if (merchant == null) {
            return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Merchant with ID ${request.merchantId} does not exist"
            )
        }

        val storeEntity = StoreEntity(
            name = request.name,
            merchant = merchant,
            description = request.description,
            location = request.location,
            managerName = request.managerName,
            contactNumber = request.contactNumber,
            openHours = request.openHours,
            closeHours = request.closeHours
        )
        storeEntity.isActive = request.isActive

        val savedStore = storeRepository.save(storeEntity)
        return ApiResult.success(
            savedStore.id,
            "Store created successfully",
        )
    }

    override suspend fun updatedStore(
        id: String,
        request: StoreRequest
    ): ApiResult<String> {
        val merchant = merchantRepository.findMerchantById(request.merchantId)
        if (merchant == null) {
            return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Merchant with ID ${request.merchantId} does not exist"
            )
        }
        val store = storeRepository.findById(id)
            .takeIf { it -> it.get().merchant.id == request.merchantId }
            ?: return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Store with ID $id does not exist or does not belong to the merchant"
            )
        val requestUpdate = store.get()
        requestUpdate.name = request.name
        requestUpdate.location = request.location
        requestUpdate.managerName = request.managerName
        requestUpdate.contactNumber = request.contactNumber
        requestUpdate.openHours = request.openHours
        requestUpdate.closeHours = request.closeHours
        requestUpdate.description = request.description
        requestUpdate.isActive = request.isActive

        val savedStore = storeRepository.save(requestUpdate)
        return ApiResult.success(
            savedStore.id,
            "Store created successfully",
        )
    }

    override suspend fun getStoreById(id: String): ApiResult<StoreDetailResponse> {
        val store = storeRepository.findById(id)
            ?: return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Store with ID $id does not exist"
            )
        val data = store.get()
        val response = StoreDetailResponse.from(data)
        return ApiResult.success(
            response,
            "Store retrieved successfully"
        )
    }

    override suspend fun getAll(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<StoreResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = StoreFilterSpecification.storeFilterSpecification(searchString)
        val storesPage = storeRepository.findAll(spec, pageable)
        val data = storesPage.map(StoreResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = storesPage.totalElements.toInt()
        )
        return response
    }

    override suspend fun deleteStoreById(id: String): ApiResult<String> {
        val store = storeRepository.findById(id)
            ?: return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Store with ID $id does not exist"
            )
        storeRepository.delete(store.get())
        return ApiResult.success(
            id,
            "Store deleted successfully"
        )
    }

    override suspend fun getAllStoreByMerchantId(merchantId: String): ApiResult<List<StoreDetailResponse>> {
        val stores = storeRepository.findAllByMerchantId(merchantId)
        val data = stores.map(StoreDetailResponse::from)
        return ApiResult.success(
            data = data,
            message = "Stores retrieved successfully"
        )
    }

}