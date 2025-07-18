package contract

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    var id: String = UUID.randomUUID().toString()
        protected set

    @CreatedDate
    @Column(name = "created_at", nullable = false, insertable = true, updatable = false)
    lateinit var createdAt: LocalDateTime
        protected set

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true, insertable = false, updatable = true)
    lateinit var updatedAt: LocalDateTime
        protected set

    @Column(name = "is_active")
    var isActive: Boolean = true
}