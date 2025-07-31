package repository.user

import entity.user.UserEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface UserRepository : BaseRepository<UserEntity, String> {
    fun findByUsername(username: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
    fun findByPhone(phone: String): UserEntity?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhone(phone: String): Boolean
    fun findByRefreshToken(refreshToken: String): UserEntity?
}
