package specification.device

import entity.device.DeviceEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object DeviceFilterSpecification {
    fun deviceFilterSpecification(search: String?): Specification<DeviceEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            if (!search.isNullOrBlank()) {
                val likePattern = "%${search.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("deviceId")), likePattern),
                        cb.like(cb.lower(root.get("deviceName")), likePattern),
                        cb.like(cb.lower(root.get("deviceType")), likePattern)
                    )
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}