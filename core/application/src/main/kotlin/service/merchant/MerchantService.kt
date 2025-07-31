package service.merchant

import request.merchant.MerchantModeRequest
import request.merchant.MerchantRequest
import request.merchant.MerchantUpdateRequest
import request.auth.LoginRequest
import response.merchant.MerchantDetailResponse
import response.merchant.MerchantLoginResponse
import response.merchant.MerchantResponse
import response.merchant.MyMerchantResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface MerchantService {
    fun merchantLogin(request: LoginRequest): ApiResult<MerchantLoginResponse>

    fun getMyMerchant(): ApiResult<MyMerchantResponse>

    fun merchantMode(request: MerchantModeRequest): ApiResult<MerchantLoginResponse>

    fun createMerchant(request: MerchantRequest): ApiResult<String>

    fun updateMerchant(id: String, request: MerchantUpdateRequest): ApiResult<String>

    fun getMerchantById(id: String): ApiResult<MerchantDetailResponse>

    fun getAllMerchants(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<MerchantResponse>

    fun deleteMerchant(merchantId: String): ApiResult<String>
}