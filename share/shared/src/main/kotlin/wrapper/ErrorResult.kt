package wrapper

import org.springframework.http.HttpStatus

data class ErrorResult(
    val message: String?,
    val statusCode: HttpStatus,
    val success: Boolean = false
)