package repository.reward

import entity.reward.UserRewardEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface RewardRepository : BaseRepository<UserRewardEntity, String> {
}