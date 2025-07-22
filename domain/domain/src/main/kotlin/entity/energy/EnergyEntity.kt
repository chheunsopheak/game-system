package entity.energy

import contract.BaseEntity
import entity.merchant.MerchantEntity
import jakarta.persistence.*

@Entity
@Table(name = "energy")
data class EnergyEntity(
    @Column(name = "value", nullable = false)
    var value: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    var merchant: MerchantEntity
) : BaseEntity() {
    constructor() : this(
        value = 0,
        merchant = MerchantEntity()
    )
}