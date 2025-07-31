package entity.energy

import contract.BaseEntity
import entity.merchant.MerchantEntity
import entity.user.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "energy_logs")
data class EnergyLogEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    var merchant: MerchantEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,

    @Column(name = "value", nullable = false)
    var value: Int,

    @Column(name = "note")
    var note: String? = null,
) : BaseEntity()