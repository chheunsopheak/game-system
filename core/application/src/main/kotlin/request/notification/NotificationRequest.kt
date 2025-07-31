package request.notification

import jakarta.annotation.Priority

data class NotificationRequest(
    val targetId: String,
    val type: String,
    val senderId: String,
    val receiverId: String,
    val title: String,
    val body: String,
    val imageUrl: String,
    val iconUrl: String,
    val priority: Int = 0,
)
