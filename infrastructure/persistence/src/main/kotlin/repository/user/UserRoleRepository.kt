package repository.user

import entity.user.UserRoleEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface UserRoleRepository : BaseRepository<UserRoleEntity, String> {
    fun findByUserId(userId: String): UserRoleEntity?
}