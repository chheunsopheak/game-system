package specification.notification

import entity.notification.NotificationEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object NotificationFilterSpecification {
    fun filterSpecification(search: String?): Specification<NotificationEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            if (!search.isNullOrBlank()) {
                val likePattern = "%${search.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("title")), likePattern),
                        cb.like(cb.lower(root.get("body")), likePattern)
                    )
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}