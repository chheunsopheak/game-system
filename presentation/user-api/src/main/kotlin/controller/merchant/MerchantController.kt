package com.gamesystem.controller.merchant

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import request.auth.LoginRequest
import request.merchant.MerchantModeRequest
import request.user.UserChangePasswordRequest
import response.merchant.MerchantLoginResponse
import response.merchant.MyMerchantResponse
import response.store.StoreDetailResponse
import service.merchant.MerchantService
import service.store.StoreService
import service.user.UserService
import wrapper.ApiResult


@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class MerchantController(
    private val merchantService: MerchantService,
    private val storeService: StoreService,
    private val userService: UserService
) {

    //Merchant login
    @PostMapping("merchant/login")
    fun merchantLogin(@RequestBody request: LoginRequest): ResponseEntity<ApiResult<MerchantLoginResponse>> {
        val response = merchantService.merchantLogin(request)
        return ResponseEntity.status(response.statusCode).body(response)
    }

    //Merchant login
    @GetMapping("my-merchant")
    fun myMerchant(): ResponseEntity<ApiResult<MyMerchantResponse>> {
        val response = merchantService.getMyMerchant()
        return ResponseEntity.status(response.statusCode).body(response)
    }

    //Merchant Change Password
    @PostMapping("merchant/change-password")
    fun merchantChangePassword(@RequestBody request: UserChangePasswordRequest): ResponseEntity<ApiResult<String>> {
        val response = userService.changePassword(request)
        return ResponseEntity.status(response.statusCode).body(response)
    }

    @PostMapping("merchant/mode")
    fun merchantMode(@RequestBody request: MerchantModeRequest): ResponseEntity<ApiResult<MerchantLoginResponse>> {
        val response = merchantService.merchantMode(request)
        return ResponseEntity.status(response.statusCode).body(response)
    }


    @GetMapping("merchant/{merchantId}/stores")
    fun getStoreItems(@PathVariable merchantId: String): ResponseEntity<ApiResult<List<StoreDetailResponse>>> {
        val response = storeService.getAllStoreByMerchantId(merchantId)
        return ResponseEntity.status(response.statusCode).body(response)
    }
}