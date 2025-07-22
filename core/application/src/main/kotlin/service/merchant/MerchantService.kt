package service.merchant

import request.merchant.MerchantModeRequest
import request.merchant.MerchantRequest
import request.merchant.MerchantUpdateRequest
import request.user.LoginRequest
import response.merchant.MerchantDetailResponse
import response.merchant.MerchantLoginResponse
import response.merchant.MerchantResponse
import response.merchant.MyMerchantResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface MerchantService {
    suspend fun merchantLogin(request: LoginRequest): ApiResult<MerchantLoginResponse>

    suspend fun getMyMerchant(): ApiResult<MyMerchantResponse>

    suspend fun merchantMode(request: MerchantModeRequest): ApiResult<MerchantLoginResponse>

    suspend fun createMerchant(request: MerchantRequest): ApiResult<String>

    suspend fun updateMerchant(id: String, request: MerchantUpdateRequest): ApiResult<String>

    suspend fun getMerchantById(id: String): ApiResult<MerchantDetailResponse>

    suspend fun getAllMerchants(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<MerchantResponse>

    suspend fun deleteMerchant(merchantId: String): ApiResult<String>
}