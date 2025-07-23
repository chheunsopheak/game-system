package repository.contest

import entity.contest.ContestEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface ContestRepository : BaseRepository<ContestEntity, String> {
}