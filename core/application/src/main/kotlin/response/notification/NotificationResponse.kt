package response.notification

import java.time.LocalDateTime

data class NotificationResponse(
    val id: String,
    val targetId: String,
    val type: String,
    val sender: UserNotificationResponse?,
    val receiver: UserNotificationResponse?,
    val title: String,
    val body: String,
    val imageUrl: String?,
    val iconUrl: String?,
    val priority: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)