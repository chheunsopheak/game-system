package specification.reward

import entity.reward.UserRewardEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object RewardFilterSpecification {
    fun rewardFilterSpecification(search: String?): Specification<UserRewardEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            if (!search.isNullOrBlank()) {
                val likePattern = "%${search.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("phone")), likePattern),
                        cb.like(cb.lower(root.get("ref")), likePattern),
                        cb.like(cb.lower(root.get("reward_name")), likePattern)
                    )
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}