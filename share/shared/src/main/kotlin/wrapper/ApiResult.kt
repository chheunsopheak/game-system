package wrapper


import org.springframework.http.HttpStatus

data class ApiResult<T>(
    val data: T?, val message: String?, val statusCode: Int, val success: Boolean
) {
    companion object {
        // Success variations
        fun <T> success(data: T?, message: String?): ApiResult<T> {
            return ApiResult(data, message ?: "Resource created successfully", HttpStatus.OK.value(), true)
        }

        // Error variations
        fun <T> error(statusCode: Int, message: String?): ApiResult<T> {
            return ApiResult(null, message, statusCode, false)
        }

        fun <T> failed(statusCode: Int, message: String?): ApiResult<T> {
            return ApiResult(null, message, statusCode, false)
        }

        fun <T> badRequest(message: String?): ApiResult<T> {
            return ApiResult(null, message, HttpStatus.BAD_REQUEST.value(), false)
        }

        fun <T> notFound(message: String?): ApiResult<T> {
            return ApiResult(null, message, HttpStatus.NOT_FOUND.value(), false)
        }

        fun <T> internalError(message: String?): ApiResult<T> {
            return ApiResult(
                null,
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                false
            )
        }
    }
}