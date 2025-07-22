package entity.user

import contract.BaseEntity
import entity.device.DeviceEntity
import entity.energy.EnergyLogEntity
import entity.history.GameHistoryEntity
import entity.merchant.MerchantEntity
import entity.reward.UserRewardEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(

    @Column(name = "name")
    var name: String,

    @Column(name = "user_name", unique = true, nullable = false)
    val userName: String,

    @Column(nullable = false)
    var passwordHash: String,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column(name = "phone")
    var phone: String?,

    @Column(name = "photo")
    var photo: String?,

    @Column(name = "energy")
    var energy: Int? = 0,

    @Column(name = "account_created", nullable = false)
    val accountCreated: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "last_login")
    var lastLogin: LocalDateTime?,

    @Column(name = "role")
    var role: Int = 0,

    @Column(name = "store_id", nullable = true)
    var storeId: String? = null,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = false)
    var merchant: MerchantEntity? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var devices: MutableList<DeviceEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var userRewards: MutableList<UserRewardEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var gamePlays: MutableList<GameHistoryEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var energyLogs: MutableList<EnergyLogEntity> = mutableListOf()
) : BaseEntity() {

    constructor() : this(
        name = "",
        userName = "",
        passwordHash = "",
        email = "",
        phone = "",
        photo = "",
        energy = 0,
        accountCreated = LocalDateTime.now(),
        lastLogin = null,
        role = 0,
        storeId = null,
        merchant = null,
        devices = mutableListOf(),
        userRewards = mutableListOf(),
        gamePlays = mutableListOf(),
        energyLogs = mutableListOf()
    )
}


