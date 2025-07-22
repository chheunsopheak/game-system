package specification.store

import entity.store.StoreEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object StoreFilterSpecification {

    fun storeFilterSpecification(search: String?): Specification<StoreEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            if (!search.isNullOrBlank()) {
                val likePattern = "%${search.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("name")), likePattern),
                        cb.like(cb.lower(root.get("location")), likePattern),
                        cb.like(cb.lower(root.get("managerName")), likePattern),
                        cb.like(cb.lower(root.get("contactNumber")), likePattern),
                        cb.like(cb.lower(root.get("description")), likePattern)
                    )
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}
