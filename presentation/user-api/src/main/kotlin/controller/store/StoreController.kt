package com.gamesystem.controller.store

import constant.BaseUrl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import service.store.StoreService

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class StoreController(private val storeService: StoreService) {

    @GetMapping("my-store/{storeId}")
    suspend fun myStore(@PathVariable storeId: String) = storeService.myStore(storeId)
}