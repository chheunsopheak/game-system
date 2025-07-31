package specification.role

import entity.role.RoleEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object RoleFilterSpecification {
    fun roleFilterSpecification(search: String?): Specification<RoleEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            if (!search.isNullOrBlank()) {
                val likePattern = "%${search.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("name")), likePattern),
                        cb.like(cb.lower(root.get("description")), likePattern),
                        cb.like(cb.lower(root.get("code")), likePattern)
                    )
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}