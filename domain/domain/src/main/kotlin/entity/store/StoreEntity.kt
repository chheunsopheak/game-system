package entity.store

import contract.BaseEntity
import entity.merchant.MerchantEntity
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "stores")
data class StoreEntity(
    @Column(name = "name", nullable = false, length = 100)
    var name: String,

    @Column(name = "logo_url")
    var logoUrl: String? = null,

    @Column(name = "cover_url")
    var coverUrl: String? = null,

    @Column(name = "location", length = 255)
    var location: String? = null,

    @Column(name = "manager_name", length = 100)
    var managerName: String? = null,

    @Column(name = "contact_number", length = 20)
    var contactNumber: String? = null,

    @Column(name = "open_hours")
    var openHours: LocalDateTime,

    @Column(name = "close_hours")
    var closeHours: LocalDateTime? = null,

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null,

    @Column(name = "verified", nullable = false)
    var verified: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    var merchant: MerchantEntity

) : BaseEntity()