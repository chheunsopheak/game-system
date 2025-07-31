package service.notification

import request.notification.NotificationRequest
import response.notification.NotificationResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface NotificationService {
    fun sendNotification(request: NotificationRequest): ApiResult<String>
    fun broadcastNotification(topic: String, request: NotificationRequest): ApiResult<String>
    fun saveNotification(request: NotificationRequest): ApiResult<String>
    fun getNotifications(
        pageNumber: Int, pageSize: Int, searchString: String?
    ): PaginatedResult<NotificationResponse>

    fun getById(id: String): ApiResult<NotificationResponse>
    fun markAsSeen(id: String): ApiResult<String>
    fun getUnSeenCount(): ApiResult<Int>
    fun deleteById(id: String): ApiResult<String>
}