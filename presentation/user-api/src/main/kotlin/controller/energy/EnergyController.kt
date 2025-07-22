package com.gamesystem.controller.energy

import constant.BaseUrl
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import request.energy.EnergyConsumeRequest
import service.energy.EnergyService


@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class EnergyController(private val energyService: EnergyService) {

    @GetMapping("merchant/{merchantId}/energy-activities")
    suspend fun getEnergyLogsByMerchantId(
        @PathVariable merchantId: String,
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ) = energyService.getEnergyLogsByMerchantId(merchantId, pageNumber, pageSize, searchString)

    @GetMapping("user/{userId}/energy-activities")
    suspend fun getEnergyLogsByUserId(
        @PathVariable userId: String,
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ) = energyService.getEnergyLogsByUserId(userId, pageNumber, pageSize, searchString)

    @GetMapping("energy-activity/{id}")
    suspend fun getEnergyLogs(
        @PathVariable id: String,
    ) = energyService.getEnergyLogById(id)

    @PostMapping("user-consume/energy")
    suspend fun consumeUserEnergy(
        @Valid @RequestBody request: EnergyConsumeRequest
    ) = energyService.consumeUserEnergy(request)
}