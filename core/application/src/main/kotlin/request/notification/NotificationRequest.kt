package request.notification

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
