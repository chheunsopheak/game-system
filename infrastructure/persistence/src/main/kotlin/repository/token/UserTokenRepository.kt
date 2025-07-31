package repository.token

import entity.user.UserTokenEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface UserTokenRepository : BaseRepository<UserTokenEntity, String> {
    fun findByUserId(userId: String): UserTokenEntity?
}