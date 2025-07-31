package entity.role

import contract.BaseEntity
import entity.user.UserRoleEntity
import jakarta.persistence.*

@Entity
@Table(name = "role")
class RoleEntity(
    @Column(unique = true, name = "name")
    var name: String,

    @Column(unique = true, name = "code")
    var code: String,

    @Column(unique = true, name = "description")
    var description: String,

    @OneToMany(
        mappedBy = "role", cascade = [CascadeType.ALL], orphanRemoval = true
    ) var userRoles: MutableList<UserRoleEntity> = mutableListOf()
) : BaseEntity()