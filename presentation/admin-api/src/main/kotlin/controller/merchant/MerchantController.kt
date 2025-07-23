package com.gamesystem.controller.merchant

import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import request.merchant.MerchantRequest
import request.merchant.MerchantUpdateRequest
import service.merchant.MerchantService

@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class MerchantController(private val merchantService: MerchantService) {

    @PostMapping("merchant")
    fun createMerchant(@RequestBody request: MerchantRequest) = merchantService.createMerchant(request)

    @PutMapping("merchant/{id}")
    fun updateMerchant(
        @PathVariable id: String,
        @RequestBody request: MerchantUpdateRequest
    ) = merchantService.updateMerchant(id, request)

    @GetMapping("merchant/{id}")
    fun getMerchantById(@PathVariable id: String) = merchantService.getMerchantById(id)

    @GetMapping("merchants")
    fun getMerchantById(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam("searchString") searchString: String?
    ) = merchantService.getAllMerchants(pageNumber, pageSize, searchString)

    @DeleteMapping("merchant/{id}")
    fun deleteMerchantById(@PathVariable id: String) = merchantService.deleteMerchant(id)

}