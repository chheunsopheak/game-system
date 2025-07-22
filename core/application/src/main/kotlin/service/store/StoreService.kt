package service.store

import request.store.StoreRequest
import response.store.MyStoreResponse
import response.store.StoreDetailResponse
import response.store.StoreResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface StoreService {
    suspend fun myStore(storeId: String): ApiResult<MyStoreResponse>
    suspend fun createStore(request: StoreRequest): ApiResult<String>
    suspend fun updatedStore(id: String, request: StoreRequest): ApiResult<String>
    suspend fun getStoreById(id: String): ApiResult<StoreDetailResponse>
    suspend fun getAll(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<StoreResponse>
    suspend fun deleteStoreById(id: String): ApiResult<String>
    suspend fun getAllStoreByMerchantId(
        merchantId: String
    ): ApiResult<List<StoreDetailResponse>>
}