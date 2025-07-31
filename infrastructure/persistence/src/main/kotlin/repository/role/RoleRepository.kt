package repository.role

import entity.role.RoleEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface RoleRepository : BaseRepository<RoleEntity, String> {
    fun findByName(name: String): RoleEntity?
}