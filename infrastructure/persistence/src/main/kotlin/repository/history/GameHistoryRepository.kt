package repository.history

import entity.history.GameHistoryEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface GameHistoryRepository : BaseRepository<GameHistoryEntity, String> {
    fun findByUser_Id(userId: String, pageable: Pageable): Page<GameHistoryEntity>
    fun findAllByUser_Id(userId: String): List<GameHistoryEntity>

}