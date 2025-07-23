package com.gamesystem.controller.store

import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import request.store.StoreRequest
import service.store.StoreService

@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class StoreController(private val storeService: StoreService) {

    @PostMapping("store")
    fun createStore(@RequestBody request: StoreRequest) = storeService.createStore(request)

    @PutMapping("store/{id}")
    fun updateStore(@PathVariable id: String, @RequestBody request: StoreRequest) =
        storeService.updatedStore(id, request)

    @GetMapping("store/{id}")
    fun getStoreById(@PathVariable id: String) = storeService.getStoreById(id)

    @GetMapping("stores")
    fun getAllStores(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam("searchString", required = false) searchString: String?
    ) = storeService.getAll(pageNumber, pageSize, searchString)

    @DeleteMapping("store/{id}")
    fun deleteStoreById(@PathVariable id: String) = storeService.deleteStoreById(id)
}