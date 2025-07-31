package entity.user

import contract.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "user_token")
class UserTokenEntity(

    @Column(name = "token", nullable = false, unique = true)
    var token: String,

    @Column(name = "is_expired", nullable = false)
    val isExpired: Boolean = false,

    @Column(name = "is_revoked", nullable = false)
    val isRevoked: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity

) : BaseEntity()