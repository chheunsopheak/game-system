package response

import org.springframework.http.HttpStatus

data class ApiResponse(
    val status: HttpStatus,
    val message: String
)