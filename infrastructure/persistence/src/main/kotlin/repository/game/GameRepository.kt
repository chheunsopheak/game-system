package repository.game

import entity.game.GameEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface GameRepository : BaseRepository<GameEntity, String> {
    fun findByName(name: String): GameEntity?
    fun existsByName(name: String): Boolean
}