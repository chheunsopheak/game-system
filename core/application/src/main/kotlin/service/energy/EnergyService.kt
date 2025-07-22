package service.energy

import request.energy.EnergyConsumeRequest
import request.energy.EnergyLogRequest
import request.energy.EnergyRequest
import response.energy.*
import wrapper.ApiResult
import wrapper.PaginatedResult

interface EnergyService {
    suspend fun createMerchantEnergy(request: EnergyRequest): ApiResult<String>
    suspend fun updateMerchantEnergy(id: String, request: EnergyRequest): ApiResult<String>
    suspend fun deleteEnergyById(id: String): ApiResult<String>
    suspend fun getEnergyById(id: String): ApiResult<EnergyDetailResponse>
    suspend fun getEnergiesByMerchantId(
        merchantId: String
    ): ApiResult<EnergyResponse>

    suspend fun consumeUserEnergy(request: EnergyConsumeRequest): ApiResult<String>

    suspend fun createMerchantEnergyLog(request: EnergyLogRequest): ApiResult<String>
    suspend fun getEnergyLogById(id: String): ApiResult<EnergyLogDetailResponse>
    suspend fun getEnergyLogsByMerchantId(
        merchantId: String,
        pageNumber: Int,
        pageSize: Int,
        searchString: String? = null
    ): PaginatedResult<EnergyLogMerchantResponse>

    suspend fun getEnergyLogsByUserId(
        userId: String,
        pageNumber: Int,
        pageSize: Int,
        searchString: String? = null
    ): PaginatedResult<EnergyLogUserResponse>
}