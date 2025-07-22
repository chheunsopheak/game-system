package specification.merchant

import entity.merchant.MerchantEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object MerchantFilterSpecification {
    fun merchantFilterSpecification(search: String?): Specification<MerchantEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            if (!search.isNullOrBlank()) {
                val likePattern = "%${search.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("phoneNumber")), likePattern),
                        cb.like(cb.lower(root.get("email")), likePattern),
                        cb.like(cb.lower(root.get("name")), likePattern)
                    )
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}