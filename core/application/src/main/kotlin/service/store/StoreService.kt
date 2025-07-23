package service.store

import request.store.StoreRequest
import response.store.MyStoreResponse
import response.store.StoreDetailResponse
import response.store.StoreResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface StoreService {
    fun myStore(storeId: String): ApiResult<MyStoreResponse>
    fun createStore(request: StoreRequest): ApiResult<String>
    fun updatedStore(id: String, request: StoreRequest): ApiResult<String>
    fun getStoreById(id: String): ApiResult<StoreDetailResponse>
    fun getAll(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<StoreResponse>
    fun deleteStoreById(id: String): ApiResult<String>
    fun getAllStoreByMerchantId(merchantId: String): ApiResult<List<StoreDetailResponse>>
}