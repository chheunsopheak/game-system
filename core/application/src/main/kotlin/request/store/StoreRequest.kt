package request.store

import java.time.LocalDateTime

data class StoreRequest(
    var merchantId: String,
    var name: String,
    var location: String? = null,
    var managerName: String? = null,
    var contactNumber: String? = null,
    var openHours: LocalDateTime,
    var closeHours: LocalDateTime? = null,
    var description: String? = null,
    var isActive: Boolean
)
