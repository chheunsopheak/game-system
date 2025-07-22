package com.gamesystem.controller.merchant

import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import request.merchant.MerchantModeRequest
import request.user.LoginRequest
import request.user.UserChangePasswordRequest
import service.merchant.MerchantService
import service.store.StoreService
import service.user.UserService


@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class MerchantController(
    private val merchantService: MerchantService,
    private val storeService: StoreService,
    private val userService: UserService
) {

    //Merchant login
    @PostMapping("merchant/login")
    suspend fun merchantLogin(@RequestBody request: LoginRequest) = merchantService.merchantLogin(request)

    //Merchant login
    @GetMapping("my-merchant")
    suspend fun myMerchant() = merchantService.getMyMerchant()

    //Merchant Change Password
    @PostMapping("merchant/change-password")
    suspend fun merchantChangePassword(@RequestBody request: UserChangePasswordRequest) =
        userService.changePassword(request)

    @PostMapping("merchant/mode")
    suspend fun merchantMode(@RequestBody request: MerchantModeRequest) = merchantService.merchantMode(request)


    @GetMapping("merchant/{merchantId}/stores")
    suspend fun getStoreItems(@PathVariable merchantId: String) = storeService.getAllStoreByMerchantId(merchantId)
}