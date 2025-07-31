package repository.notification

import entity.notification.NotificationEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface NotificationRepository : BaseRepository<NotificationEntity, String> {
    fun countByReceiverIdAndIsSeen(receiverId: String, seen: Boolean): Int
}