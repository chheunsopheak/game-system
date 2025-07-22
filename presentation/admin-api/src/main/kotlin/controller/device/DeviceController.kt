package com.gamesystem.controller.device

import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import request.device.DeviceRequest
import service.device.DeviceService


@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class DeviceController(private val deviceService: DeviceService) {

    //Get all devices
    @GetMapping("devices")
    suspend fun getAllDevices(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ) = deviceService.getAll(pageNumber, pageSize, searchString)

    //Get device by id
    @GetMapping("device/{id}")
    suspend fun getDeviceById(@PathVariable id: String) = deviceService.getById(id)

    //Create Device
    @PostMapping("device")
    suspend fun addDevice(@RequestBody request: DeviceRequest) = deviceService.addDevice(request)

    //Update Device
    @PutMapping("device/{id}")
    suspend fun updateDevice(@PathVariable id: String, @RequestBody request: DeviceRequest) =
        deviceService.updateDevice(id, request)

    //Delete Device
    @DeleteMapping("device/{id}")
    suspend fun deleteDevice(@PathVariable id: String) = deviceService.deleteDevice(id)
}