package entity.merchant

import contract.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "merchants")
class MerchantEntity(
    @Column(name = "name", nullable = false)
    var name: String,
) : BaseEntity()