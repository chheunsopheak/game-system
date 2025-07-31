package wrapper

import org.springframework.http.HttpStatus

data class PaginatedResult<T>(
    val data: List<T>?,
    val currentPage: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
    val statusCode: Int,
    val message: String,
    val succeeded: Boolean
) {
    companion object {
        fun <T> success(
            data: List<T>?,
            pageNumber: Int,
            pageSize: Int,
            totalItems: Int,
            message: String = "Data retrieved successfully"
        ): PaginatedResult<T> {
            val totalPages = if (pageSize > 0) ((totalItems + pageSize - 1) / pageSize) else 1
            return PaginatedResult(
                data = data,
                currentPage = pageNumber,
                pageSize = pageSize,
                totalItems = totalItems,
                totalPages = totalPages,
                hasPreviousPage = pageNumber > 1,
                hasNextPage = pageNumber < totalPages,
                statusCode = HttpStatus.OK.value(),
                message = message,
                succeeded = true
            )
        }

        fun <T> error(
            message: String,
            statusCode: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
            data: List<T>? = null
        ): PaginatedResult<T> {
            return PaginatedResult(
                data = data,
                currentPage = 0,
                pageSize = 0,
                totalItems = 0,
                totalPages = 0,
                hasPreviousPage = false,
                hasNextPage = false,
                statusCode = statusCode.value(),
                message = message,
                succeeded = false
            )
        }

    }
}