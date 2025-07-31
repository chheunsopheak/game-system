package service.device

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.device.DeviceRepository
import repository.user.UserRepository
import request.device.DeviceRequest
import response.device.DeviceDetailResponse
import response.device.DeviceResponse
import service.user.UserService
import specification.device.DeviceFilterSpecification
import wrapper.ApiResult
import wrapper.PaginatedResult

@Service
class DeviceServiceImpl(
    private val deviceRepository: DeviceRepository,
    private val userService: UserService,
    private val userRepository: UserRepository
) : DeviceService {
    override fun getById(id: String): ApiResult<DeviceDetailResponse> {
        val device = deviceRepository.findById(id)
        if (device.isEmpty)
            return ApiResult.failed(HttpStatus.NOT_FOUND.value(), "Device with id $id not found")
        val data = DeviceDetailResponse.from(device.get())
        return ApiResult.success(data, "Device retrieved successfully")
    }

    override fun getAll(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<DeviceResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = DeviceFilterSpecification.deviceFilterSpecification(searchString)
        val devicesPage = deviceRepository.findAll(spec, pageable)
        val data = devicesPage.map(DeviceResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = devicesPage.totalElements.toInt()
        )
        return response
    }

//    override fun addDevice(request: DeviceRequest): ApiResult<String> {
//        val requestAddUser = CreateUserRequest(
//            username = NumGenerator.generateNo("D"),
//            email = NumGenerator.generateNo("D"),
//            password = request.password,
//            name = request.deviceName,
//            photo = request.photo ?: "",
//            phone = "855" + NumGenerator.generateNo("5"),
//            role = 1
//        )
//        val savedUser = userService.userRegister(requestAddUser)
//        if (savedUser.statusCode != HttpStatus.OK.value()) {
//            return ApiResult.failed(
//                savedUser.statusCode,
//                savedUser.message ?: "Failed to save user"
//            )
//        }
//        val user = userRepository.findById(savedUser.data!!.userId)
//
//        if (user == null) {
//            return ApiResult.failed(
//                HttpStatus.NOT_FOUND.value(),
//                "Failed to save user"
//            )
//        }
//        val requestDevice = DeviceEntity(
//            deviceId = request.deviceId,
//            deviceName = request.deviceName,
//            deviceType = request.deviceType,
//            osVersion = request.osVersion,
//            ipAddress = request.ipAddress,
//            lastOnlineAt = LocalDateTime.now(),
//            registeredAt = LocalDateTime.now(),
//            user = user.get()
//        )
//        deviceRepository.save(requestDevice)
//        return ApiResult.success(requestDevice.id, "Device added successfully")
//    }

    override fun updateDevice(
        id: String,
        request: DeviceRequest
    ): ApiResult<String> {
        val device = deviceRepository.findById(id)
        if (device == null) {
            return ApiResult.failed(
                HttpStatus.NOT_FOUND.value(),
                "We couldn't find a device with the id $id."
            )
        }
        val requestUpdate = device.get()
        requestUpdate.deviceId = request.deviceId
        requestUpdate.deviceName = request.deviceName
        requestUpdate.deviceType = request.deviceType
        requestUpdate.osVersion = request.osVersion
        requestUpdate.ipAddress = request.ipAddress
        deviceRepository.save(requestUpdate)
        return ApiResult.success(requestUpdate.id, "Device updated successfully")
    }

    override fun deleteDevice(id: String): ApiResult<String> {
        val device = deviceRepository.findById(id)
        if (device == null) {
            return ApiResult.failed(
                HttpStatus.NOT_FOUND.value(),
                "We couldn't find a device with the id $id."
            )
        }
        deviceRepository.deleteById(id)
        return ApiResult.success(id, "Device deleted successfully")
    }
}