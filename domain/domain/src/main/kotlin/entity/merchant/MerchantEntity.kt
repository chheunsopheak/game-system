package entity.merchant

import contract.BaseEntity
import entity.energy.EnergyEntity
import entity.energy.EnergyLogEntity
import entity.store.StoreEntity
import entity.user.UserEntity
import jakarta.persistence.*


@Entity
@Table(name = "merchants")
class MerchantEntity(
    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "email", unique = true)
    var email: String,

    @Column(name = "phone_number")
    var phoneNumber: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "logo_url")
    var logoUrl: String? = null,

    @Column(name = "cover_url")
    var coverUrl: String? = null,

    @Column(name = "address")
    var address: String? = null,

    @Column(name = "verified", nullable = false)
    var verified: Boolean = false,

    @OneToMany(mappedBy = "merchant", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var stores: MutableList<StoreEntity> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    var user: UserEntity,

    @OneToMany(mappedBy = "merchant", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var energies: MutableList<EnergyEntity> = mutableListOf(),

    @OneToMany(mappedBy = "merchant", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var energyLogs: MutableList<EnergyLogEntity> = mutableListOf()

) : BaseEntity() {
    constructor() : this(
        name = "",
        email = "",
        phoneNumber = null,
        description = null,
        logoUrl = null,
        coverUrl = null,
        address = null,
        verified = false,
        user = UserEntity()
    )
}