package entity.user

import contract.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class UserEntity(
    @Column(name = "username", nullable = false, unique = true)
    var username: String,
    @Column(name = "password", nullable = false)
    var password: String,
    @Column(name = "email", nullable = false, unique = true)
    var email: String,
    @Column(name = "name", nullable = false)
    var name: String
) : BaseEntity()