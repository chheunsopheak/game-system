package entity.notification

import contract.BaseEntity
import entity.user.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "notification")
class NotificationEntity(
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(
        name = "sender_id", insertable = false, updatable = false
    ) var sender: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(
        name = "receiver_id", insertable = false, updatable = false
    ) var receiver: UserEntity? = null,

    @Column(name = "target_id", nullable = false) val targetId: String,

    @Column(name = "type", nullable = false) val type: String,

    @Column(name = "title", nullable = false) val title: String,

    @Column(name = "body", nullable = false) val body: String,

    @Column(name = "icon_url", nullable = false) val iconUrl: String = "",

    @Column(name = "image_url", nullable = false) val imageUrl: String = "",

    @Column(name = "is_seen", nullable = false) var isSeen: Boolean = false,

    @Column(name = "is_new", nullable = false) var isNew: Boolean = true,

    @Column(name = "is_deleted", nullable = false) var isDeleted: Boolean = false,

    @Column(name = "priority", nullable = false) val priority: Int = 0

) : BaseEntity()