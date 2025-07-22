package service.device

import request.device.DeviceRequest
import response.device.DeviceDetailResponse
import response.device.DeviceResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface DeviceService {
    suspend fun getById(id: String): ApiResult<DeviceDetailResponse>
    suspend fun getAll(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<DeviceResponse>
    suspend fun addDevice(request: DeviceRequest): ApiResult<String>
    suspend fun updateDevice(id: String, request: DeviceRequest): ApiResult<String>
    suspend fun deleteDevice(id: String): ApiResult<String>
}