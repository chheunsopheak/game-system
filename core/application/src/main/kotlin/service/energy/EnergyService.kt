package service.energy

import request.energy.EnergyConsumeRequest
import request.energy.EnergyLogRequest
import request.energy.EnergyRequest
import response.energy.*
import wrapper.ApiResult
import wrapper.PaginatedResult

interface EnergyService {
    fun createMerchantEnergy(request: EnergyRequest): ApiResult<String>
    fun updateMerchantEnergy(id: String, request: EnergyRequest): ApiResult<String>
    fun deleteEnergyById(id: String): ApiResult<String>
    fun getEnergyById(id: String): ApiResult<EnergyDetailResponse>
    fun getEnergiesByMerchantId(
        merchantId: String
    ): ApiResult<EnergyResponse>

    fun consumeUserEnergy(request: EnergyConsumeRequest): ApiResult<String>

    fun createMerchantEnergyLog(request: EnergyLogRequest): ApiResult<String>
    fun getEnergyLogById(id: String): ApiResult<EnergyLogDetailResponse>
    fun getEnergyLogsByMerchantId(
        merchantId: String,
        pageNumber: Int,
        pageSize: Int,
        searchString: String? = null
    ): PaginatedResult<EnergyLogMerchantResponse>

    fun getEnergyLogsByUserId(
        userId: String,
        pageNumber: Int,
        pageSize: Int,
        searchString: String? = null
    ): PaginatedResult<EnergyLogUserResponse>
}