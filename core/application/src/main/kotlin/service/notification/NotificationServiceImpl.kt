package service.notification

import entity.notification.NotificationEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.notification.NotificationRepository
import repository.user.UserRepository
import request.FirebaseBroadcastRequest
import request.FirebaseRequest
import request.notification.NotificationRequest
import response.notification.NotificationResponse
import response.notification.UserNotificationResponse
import service.FirebaseService
import service.user.UserService
import specification.notification.NotificationFilterSpecification
import wrapper.ApiResult
import wrapper.PaginatedResult

@Service
class NotificationServiceImpl(
    private val notification: NotificationRepository,
    private val firebaseService: FirebaseService,
    private val userRepository: UserRepository,
    private val userService: UserService
) : NotificationService {
    override fun sendNotification(request: NotificationRequest): ApiResult<String> {
        val tokens = listOf<String>()
        val requestNotification = FirebaseRequest(
            token = tokens,
            title = request.title,
            body = request.body,
            data = mapOf(
                "type" to request.type,
                "targetId" to request.targetId,
                "senderId" to request.senderId,
                "receiverId" to request.receiverId,
                "iconUrl" to request.iconUrl,
                "imageUrl" to request.imageUrl,
                "priority" to request.priority.toString()
            )
        )
        val response = firebaseService.sendNotification(requestNotification)
        if (response.success) {
            return ApiResult.success(
                data = response.message, message = "Notification sent successfully"
            )
        }
        return ApiResult.failed(
            HttpStatus.BAD_REQUEST.value(), message = "Failed to send notification: ${response.message}"
        )
    }

    override fun broadcastNotification(topic: String, request: NotificationRequest): ApiResult<String> {
        val requestNotification = FirebaseBroadcastRequest(
            topic = topic,
            title = request.title,
            body = request.body,
            data = mapOf(
                "type" to request.type,
                "targetId" to request.targetId,
                "senderId" to request.senderId,
                "receiverId" to request.receiverId,
                "iconUrl" to request.iconUrl,
                "imageUrl" to request.imageUrl,
                "priority" to request.priority.toString()
            )
        )
        val response = firebaseService.sendBroadcastNotification(requestNotification)
        if (response.success) {
            return ApiResult.success(
                data = response.message, message = "Broadcast notification sent successfully"
            )
        }
        return ApiResult.failed(
            HttpStatus.BAD_REQUEST.value(), message = "Failed to send broadcast notification: ${response.message}"
        )
    }

    override fun saveNotification(request: NotificationRequest): ApiResult<String> {
        val notificationRequest = NotificationEntity(
            sender = userRepository.findById(request.senderId).get(),
            receiver = userRepository.findById(request.receiverId).get(),
            targetId = request.targetId,
            type = request.type,
            title = request.title,
            body = request.body,
            iconUrl = request.iconUrl,
            imageUrl = request.imageUrl,
            isSeen = false,
            isNew = true,
            isDeleted = false,
            priority = 1
        )
        val savedNotification = notification.save(notificationRequest)
        if (savedNotification == null) {
            return ApiResult.failed(
                HttpStatus.BAD_REQUEST.value(), message = "Failed to save notification"
            )
        }
        return ApiResult.success(
            data = savedNotification.id, message = "Notification saved successfully"
        )
    }

    override fun getNotifications(
        pageNumber: Int, pageSize: Int, searchString: String?
    ): PaginatedResult<NotificationResponse> {
        val receiver = userService.getMe().data
        if (receiver == null) {
            return PaginatedResult.error(
                message = "User not authenticated",
                statusCode = HttpStatus.UNAUTHORIZED,
            )
        }
        val receiverId = receiver.id
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = NotificationFilterSpecification.filterSpecification(searchString)
        val notifications = notification.findAll(spec, pageable)
        val data = notifications.content
            .filter { it.receiver != null && it.receiver!!.id == receiverId && !it.isDeleted }
            .sortedByDescending { it.createdAt }.map { notificationEntity ->
                NotificationResponse(
                    id = notificationEntity.id,
                    targetId = notificationEntity.targetId,
                    type = notificationEntity.type,
                    sender = notificationEntity.sender?.let {
                        UserNotificationResponse(
                            id = it.id, name = it.name, photoUrl = it.photoUrl
                        )
                    },
                    receiver = notificationEntity.receiver?.let {
                        UserNotificationResponse(
                            id = it.id, name = it.name, photoUrl = it.photoUrl
                        )
                    },
                    title = notificationEntity.title,
                    body = notificationEntity.body,
                    imageUrl = notificationEntity.imageUrl,
                    iconUrl = notificationEntity.iconUrl,
                    priority = notificationEntity.priority,
                    createdAt = notificationEntity.createdAt,
                    updatedAt = notificationEntity.updatedAt
                )
            }
        val response = PaginatedResult.success(
            data = data,
            pageSize = pageSize,
            pageNumber = pageNumber,
            totalItems = notifications.totalElements.toInt(),
            message = "Notifications retrieved successfully"
        )
        return response
    }

    override fun getById(id: String): ApiResult<NotificationResponse> {
        val record = notification.findById(id)
        if (record.isEmpty) {
            return ApiResult.notFound(message = "Notification with ID $id not found")
        }
        val notificationEntity = record.get()
        if (notificationEntity.isDeleted) {
            return ApiResult.notFound(message = "Notification with ID $id has been deleted")
        }
        val response = NotificationResponse(
            id = notificationEntity.id,
            targetId = notificationEntity.targetId,
            type = notificationEntity.type,
            sender = notificationEntity.sender?.let {
                UserNotificationResponse(
                    id = it.id, name = it.name, photoUrl = it.photoUrl
                )
            },
            receiver = notificationEntity.receiver?.let {
                UserNotificationResponse(
                    id = it.id, name = it.name, photoUrl = it.photoUrl
                )
            },
            title = notificationEntity.title,
            body = notificationEntity.body,
            imageUrl = notificationEntity.imageUrl,
            iconUrl = notificationEntity.iconUrl,
            priority = notificationEntity.priority,
            createdAt = notificationEntity.createdAt,
            updatedAt = notificationEntity.updatedAt
        )
        return ApiResult.success(
            data = response, message = "Notification with ID $id retrieved successfully"
        )
    }

    override fun markAsSeen(id: String): ApiResult<String> {
        val existingRecord = notification.findById(id)

        if (existingRecord.isEmpty) {
            return ApiResult.notFound(message = "Notification with ID $id not found")
        }
        val notificationEntity = existingRecord.get()
        if (notificationEntity.isDeleted) {
            return ApiResult.notFound(message = "Notification with ID $id has been deleted")
        }
        notificationEntity.isSeen = true
        notificationEntity.isNew = false
        notification.save(notificationEntity)

        return ApiResult.success(
            data = id, message = "Notification marked as seen successfully"
        )
    }

    override fun getUnSeenCount(): ApiResult<Int> {
        val receiverId = userService.getMe().data
        if (receiverId == null) {
            return ApiResult.failed(
                HttpStatus.UNAUTHORIZED.value(), message = "User not authenticated"
            )
        }
        val unseenCount = notification.countByReceiverIdAndIsSeen(receiverId.id, false)
        return ApiResult.success(
            data = unseenCount, message = "Unseen notifications count retrieved successfully"
        )
    }

    override fun deleteById(id: String): ApiResult<String> {
        val existingRecord = notification.findById(id)

        if (existingRecord.isEmpty) {
            return ApiResult.notFound(message = "Notification with ID $id not found")
        }
        val notificationEntity = existingRecord.get()
        notificationEntity.isDeleted = true
        notificationEntity.isSeen = true
        notificationEntity.isNew = false
        notification.save(notificationEntity)

        return ApiResult.success(
            data = id, message = "Notification deleted successfully"
        )
    }
}