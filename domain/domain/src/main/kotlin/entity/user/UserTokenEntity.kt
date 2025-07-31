package entity.user

import contract.BaseEntity
import jakarta.persistence.*

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