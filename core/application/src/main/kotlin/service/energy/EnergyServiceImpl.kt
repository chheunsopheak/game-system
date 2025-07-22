package service.energy

import common.OperationEnum
import entity.energy.EnergyEntity
import entity.energy.EnergyLogEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.energy.EnergyLogRepository
import repository.energy.EnergyRepository
import repository.merchant.MerchantRepository
import repository.user.UserRepository
import request.energy.EnergyConsumeRequest
import request.energy.EnergyLogRequest
import request.energy.EnergyRequest
import response.energy.*
import wrapper.ApiResult
import wrapper.PaginatedResult

@Service
class EnergyServiceImpl(
    private val merchantRepository: MerchantRepository,
    private val energyRepository: EnergyRepository,
    private val energyLogRepository: EnergyLogRepository,
    private val userRepository: UserRepository
) : EnergyService {
    override suspend fun createMerchantEnergy(request: EnergyRequest): ApiResult<String> {
        val merchant = merchantRepository.findMerchantById(request.merchantId)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND,
                "Merchant not found"
            )
        val requestAdd = EnergyEntity(
            merchant = merchant,
            value = request.value,
        )
        requestAdd.isActive = request.isActive
        val save = energyRepository.save(requestAdd)

        return ApiResult.success(
            "Energy created successfully",
            save.id
        )
    }

    override suspend fun updateMerchantEnergy(
        id: String,
        request: EnergyRequest
    ): ApiResult<String> {
        val energy = energyRepository.findById(id)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND,
                "Energy not found"
            )
        val requestUpdate = energy.get()

        requestUpdate.value = request.value
        requestUpdate.isActive = request.isActive

        val saved = energyRepository.save(requestUpdate)
        return ApiResult.success(
            saved.id,
            "Energy updated successfully"
        )
    }

    override suspend fun deleteEnergyById(id: String): ApiResult<String> {
        val energy = energyRepository.findById(id)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND,
                "Energy not found"
            )
        energyRepository.deleteById(energy.get().id)
        return ApiResult.success(
            null,
            "Energy deleted successfully"
        )
    }

    override suspend fun getEnergyById(id: String): ApiResult<EnergyDetailResponse> {
        val energy = energyRepository.findById(id)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND,
                "Energy not found"
            )
        val response = EnergyDetailResponse.from(energy.get())
        return ApiResult.success(
            response,
            "Energy retrieved successfully"
        )
    }

    override suspend fun getEnergiesByMerchantId(merchantId: String): ApiResult<EnergyResponse> {
        val energies = energyRepository.findByMerchant_Id(merchantId)
        val response = EnergyResponse.from(energies!!)
        return ApiResult.success(
            response,
            "Energies retrieved successfully"
        )
    }

    override suspend fun consumeUserEnergy(request: EnergyConsumeRequest): ApiResult<String> {
        val merchantEnergy = energyRepository.findByMerchant_Id(request.merchantId)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND,
                "Merchant energy not found for the given user"
            )

        val user = userRepository.findById(request.userId)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND,
                "User not found"
            )

        // Validate and apply operation
        when (request.operation) {
            OperationEnum.ADD -> {
                if (merchantEnergy.value < request.energy) {
                    return ApiResult.failed(
                        HttpStatus.BAD_REQUEST,
                        "Cannot top up, Energy available: ${merchantEnergy.value}, requested: ${request.energy}"
                    )
                }

                user.get().energy = (user.get().energy ?: 0) + request.energy
                merchantEnergy.value = merchantEnergy.value - request.energy
            }

            OperationEnum.SUBTRACT -> {
                if ((user.get().energy ?: 0) < request.energy) {
                    return ApiResult.failed(
                        HttpStatus.BAD_REQUEST,
                        "Cannot subtract, User energy: ${user.get().energy}, requested: ${request.energy}"
                    )
                }

                user.get().energy = (user.get().energy ?: 0) - request.energy
                merchantEnergy.value = merchantEnergy.value + request.energy

            }
        }

        userRepository.save(user.get())
        energyRepository.save(merchantEnergy)

        // Save energy log
        createMerchantEnergyLog(
            EnergyLogRequest(
                merchantId = request.merchantId,
                userId = request.userId,
                value = request.energy,
                note = request.operation.ordinal.toString()
            )
        )

        return ApiResult.success(
            user.get().energy.toString(),
            "User energy ${request.operation} successfully"

        )
    }

    override suspend fun createMerchantEnergyLog(request: EnergyLogRequest): ApiResult<String> {
        val merchant = merchantRepository.findMerchantById(request.merchantId)
            ?: return ApiResult.failed(HttpStatus.NOT_FOUND, "Merchant not found")

        val user = userRepository.findById(request.userId)
            ?: return ApiResult.failed(HttpStatus.NOT_FOUND, "User not found")
        val requestEnergyLog = EnergyLogEntity(
            merchant = merchant,
            user = user.get(),
            value = request.value,
            note = request.note
        )
        val savedLog = energyLogRepository.save(requestEnergyLog)

        return ApiResult.success(
            savedLog.id,
            "Energy log created successfully",
        )
    }

    override suspend fun getEnergyLogById(id: String): ApiResult<EnergyLogDetailResponse> {
        val energyLog = energyLogRepository.findById(id)
            ?: return ApiResult.failed(HttpStatus.NOT_FOUND, "Energy log not found")

        val response = EnergyLogDetailResponse.from(energyLog.get())
        return ApiResult.success(
            response,
            "Energy log retrieved successfully"
        )

    }

    override suspend fun getEnergyLogsByMerchantId(
        merchantId: String,
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<EnergyLogMerchantResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val energyLogsPage = energyLogRepository.findAllByMerchantId(merchantId, pageable)
        val data = energyLogsPage
            .filter {
                searchString.isNullOrEmpty()
                        || it.user.name.contains(searchString)
                        || it.user.email.contains(searchString)
                        || it.user.phone?.contains(searchString)!!
                        || it.note?.contains(searchString)!!
            }
            .map(EnergyLogMerchantResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = energyLogsPage.totalElements.toInt()
        )
        return response
    }

    override suspend fun getEnergyLogsByUserId(
        userId: String,
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<EnergyLogUserResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val energyLogsPage = energyLogRepository.findAllByUserId(userId, pageable)
        val data = energyLogsPage
            .filter {
                searchString.isNullOrEmpty()
                        || it.user.name.contains(searchString)
                        || it.user.email.contains(searchString)
                        || it.user.phone?.contains(searchString)!!
                        || it.note?.contains(searchString)!!
            }
            .map(EnergyLogUserResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = energyLogsPage.totalElements.toInt()
        )
        return response
    }
}