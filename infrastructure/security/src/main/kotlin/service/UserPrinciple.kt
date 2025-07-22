package service

import entity.user.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrinciple(
    private val user: UserEntity
) : UserDetails {

    companion object {
        fun build(user: UserEntity): UserPrinciple {
            return UserPrinciple(user)
        }
    }

    fun getId(): String = this.user.id

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val roleName = when (user.role) {
            1 -> "USER"
            2 -> "ADMIN"
            0 -> "DEVICE"
            else -> "UNKNOWN"
        }
        return listOf(SimpleGrantedAuthority("ROLE_$roleName"))
    }

    override fun getPassword(): String = user.passwordHash
    override fun getUsername(): String = user.username
    override fun isEnabled(): Boolean = user.isActive

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
}
