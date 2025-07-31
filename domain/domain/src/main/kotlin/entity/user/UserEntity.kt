package entity.user

import contract.BaseEntity
import entity.device.DeviceEntity
import entity.energy.EnergyLogEntity
import entity.history.GameHistoryEntity
import entity.merchant.MerchantEntity
import entity.notification.NotificationEntity
import entity.reward.UserRewardEntity
import entity.user.UserTokenEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(

    @Column(name = "name") var name: String,

    @Column(name = "username", unique = true, nullable = false) val username: String,

    @Column(name = "password_hash", nullable = false) var passwordHash: String,

    @Column(name = "email", unique = true, nullable = false) var email: String,

    @Column(name = "phone") var phone: String,

    @Column(name = "photo") var photoUrl: String?,

    @Column(name = "cover") var coverUrl: String?,

    @Column(name = "energy") var energy: Int? = 0,

    @Column(name = "account_created", nullable = false) val accountCreated: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "last_login") var lastLogin: LocalDateTime?,

    @Column(name = "role") var role: Int = 0,

    @Column(name = "refresh_token") var refreshToken: String = "",

    @Column(name = "refresh_token_expires_at") var refreshTokenExpiresAt: LocalDateTime? = null,

    @Column(name = "is_enabled") var isEnabled: Boolean = true,

    @Column(name = "is_locked") var isLocked: Boolean = false,

    @Column(name = "lock_until") var lockUntil: LocalDateTime? = null,

    @Column(name = "is_deleted") var isDeleted: Boolean = false,

    @Column(name = "deleted_at") var deletedAt: LocalDateTime? = null,

    @Column(name = "access_fail_count") var accessFailCount: Int = 0,

    @Column(name = "store_id", nullable = true) var storeId: String? = null,

    @OneToOne(
        mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = false
    ) var merchant: MerchantEntity? = null,

    @OneToMany(
        mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY
    ) var devices: MutableList<DeviceEntity> = mutableListOf(),

    @OneToMany(
        mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true
    ) var userRewards: MutableList<UserRewardEntity> = mutableListOf(),

    @OneToMany(
        mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true
    ) var gamePlays: MutableList<GameHistoryEntity> = mutableListOf(),

    @OneToMany(
        mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true
    ) var energyLogs: MutableList<EnergyLogEntity> = mutableListOf(),

    @OneToMany(
        mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true
    ) var tokens: MutableList<UserTokenEntity> = mutableListOf(),

    @OneToMany(
        mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true
    ) var userRoles: MutableList<UserRoleEntity> = mutableListOf(),

    @OneToMany(
        mappedBy = "sender", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true
    )
    var sender: MutableList<NotificationEntity> = mutableListOf(),

    @OneToMany(
        mappedBy = "receiver", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true
    )
    var receiver: MutableList<NotificationEntity> = mutableListOf()

) : BaseEntity()

