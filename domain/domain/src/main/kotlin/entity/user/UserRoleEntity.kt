package entity.user

import contract.BaseEntity
import entity.role.RoleEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_roles")
class UserRoleEntity(

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") val user: UserEntity?,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "role_id") val role: RoleEntity?

) : BaseEntity()