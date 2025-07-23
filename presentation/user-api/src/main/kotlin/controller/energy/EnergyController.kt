package com.gamesystem.controller.energy

import constant.BaseUrl
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import request.energy.EnergyConsumeRequest
import response.energy.EnergyLogDetailResponse
import response.energy.EnergyLogMerchantResponse
import response.energy.EnergyLogUserResponse
import service.energy.EnergyService
import wrapper.ApiResult
import wrapper.PaginatedResult


@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class EnergyController(private val energyService: EnergyService) {

    @GetMapping("merchant/{merchantId}/energy-activities")
    fun getEnergyLogsByMerchantId(
        @PathVariable merchantId: String,
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ): ResponseEntity<PaginatedResult<EnergyLogMerchantResponse>> {
        val request = energyService.getEnergyLogsByMerchantId(merchantId, pageNumber, pageSize, searchString)
        return ResponseEntity.status(request.statusCode).body(request)
    }

    @GetMapping("user/{userId}/energy-activities")
    fun getEnergyLogsByUserId(
        @PathVariable userId: String,
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ): ResponseEntity<PaginatedResult<EnergyLogUserResponse>> {
        val request = energyService.getEnergyLogsByUserId(userId, pageNumber, pageSize, searchString)
        return ResponseEntity.status(request.statusCode).body(request)
    }

    @GetMapping("energy-activity/{id}")
    fun getEnergyLogs(
        @PathVariable id: String,
    ): ResponseEntity<ApiResult<EnergyLogDetailResponse>> {
        val request = energyService.getEnergyLogById(id)
        return ResponseEntity.status(request.statusCode).body(request)
    }

    @PostMapping("user-consume/energy")
    fun consumeUserEnergy(
        @Valid @RequestBody request: EnergyConsumeRequest
    ): ResponseEntity<ApiResult<String>> {
        val result = energyService.consumeUserEnergy(request)
        return ResponseEntity.status(result.statusCode).body(result)
    }
}