package specification.user

import entity.user.UserEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object UserFilterSpecification {
    fun userFilterSpecification(search: String?): Specification<UserEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            if (!search.isNullOrBlank()) {
                val likePattern = "%${search.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("username")), likePattern),
                        cb.like(cb.lower(root.get("email")), likePattern),
                        cb.like(cb.lower(root.get("name")), likePattern)
                    )
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}