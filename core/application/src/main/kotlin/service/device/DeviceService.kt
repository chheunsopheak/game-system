package service.device

import request.device.DeviceRequest
import response.device.DeviceDetailResponse
import response.device.DeviceResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface DeviceService {
    fun getById(id: String): ApiResult<DeviceDetailResponse>
    fun getAll(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<DeviceResponse>

    //fun addDevice(request: DeviceRequest): ApiResult<String>
    fun updateDevice(id: String, request: DeviceRequest): ApiResult<String>
    fun deleteDevice(id: String): ApiResult<String>
}