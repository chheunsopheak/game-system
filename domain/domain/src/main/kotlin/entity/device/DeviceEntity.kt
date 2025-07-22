package entity.device

import contract.BaseEntity
import entity.user.UserEntity
import jakarta.persistence.*
import net.minidev.json.annotate.JsonIgnore
import java.time.LocalDateTime


@Entity
@Table(name = "devices")
class DeviceEntity(

    @Column(name = "device_id")
    var deviceId: String,

    @Column(name = "device_name")
    var deviceName: String,

    @Column(name = "device_type")
    var deviceType: String,

    @Column(name = "os_version")
    var osVersion: String,

    @Column(name = "last_online_at")
    var lastOnlineAt: LocalDateTime,

    @Column(name = "registered_at")
    val registeredAt: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "ip_address")
    var ipAddress: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    var user: UserEntity,
) : BaseEntity() {
    constructor() : this(
        deviceId = "",
        deviceName = "",
        deviceType = "",
        osVersion = "",
        lastOnlineAt = LocalDateTime.now(),
        ipAddress = "",
        user = UserEntity(),
    )
}
