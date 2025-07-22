package specification.game

import entity.game.GameEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object GameFilterSpecification {
    fun gameFilterSpecification(search: String?): Specification<GameEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            if (!search.isNullOrBlank()) {
                val likePattern = "%${search.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("name")), likePattern),
                        cb.like(cb.lower(root.get("description")), likePattern),
                        cb.like(cb.lower(root.get("category")), likePattern)


                    )
                )
            }

            cb.and(*predicates.toTypedArray())
        }

    }
}