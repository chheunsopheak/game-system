package com.gamesystem.exception

import jakarta.servlet.http.HttpServletRequest
import org.apache.coyote.BadRequestException
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.UnsupportedMediaTypeStatusException
import org.springframework.web.servlet.NoHandlerFoundException
import wrapper.ApiResult
import wrapper.ErrorResult
import java.util.concurrent.TimeoutException
import java.util.logging.Logger
import javax.security.sasl.AuthenticationException


@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger: Logger = Logger.getLogger(GlobalExceptionHandler::class.java.name)

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ApiResult<ErrorResult> {
        val errorMessage = "An error occurred: ${ex.message}"
        logger.warning(errorMessage)
        return ApiResult.badRequest(errorMessage)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ApiResult<ErrorResult> {
        val errorMessage =
            "Invalid value '${ex.value}' for parameter '${ex.name}' - expected type: ${ex.requiredType?.simpleName}"
        logger.warning(errorMessage)
        return ApiResult.badRequest(errorMessage)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ApiResult<ErrorResult> {
        val errorMessage = ex.localizedMessage ?: "Malformed JSON request"
        logger.warning(errorMessage)
        return ApiResult.badRequest(errorMessage)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ApiResult<ErrorResult> {
        logger.warning(ex.message)
        return ApiResult.badRequest(ex.message)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(ex: NoHandlerFoundException): ApiResult<ErrorResult> {
        val errorMessage = "No handler found for request: ${ex.requestURL}"
        logger.warning(errorMessage)
        return ApiResult.notFound(errorMessage)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ApiResult<ErrorResult> {
        val errorMessage = "Access denied: ${ex.message}"
        logger.warning(errorMessage)
        return ApiResult.error(HttpStatus.FORBIDDEN.value(), errorMessage)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleUnauthorized(
        ex: BadCredentialsException, request: HttpServletRequest
    ): ApiResult<ErrorResult> {
        logger.warning("Unauthorized access attempt: ${request.requestURI} - ${ex.message}")
        return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: ${ex.message}")
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ApiResult<ErrorResult> {
        val errorMessage = "Authentication failed: ${ex.message}"
        logger.warning(errorMessage)
        return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), errorMessage)
    }

    @ExceptionHandler(HttpClientErrorException::class)
    fun handleUnauthorizedException(ex: HttpClientErrorException): ApiResult<ErrorResult> {
        val errorMessage = "You are not authorized to access this resource: ${ex.message}"
        logger.warning(errorMessage)
        return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), errorMessage)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ApiResult<ErrorResult> {
        val errorMessage = "Constraint violation: ${ex.message}"
        logger.warning(errorMessage)
        return ApiResult.badRequest(errorMessage)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ApiResult<ErrorResult> {
        val errorMessage = "Database integrity violation: ${ex.message}"
        logger.warning(errorMessage)
        return ApiResult.badRequest(errorMessage)
    }

    @ExceptionHandler(TimeoutException::class)
    fun handleTimeoutException(ex: TimeoutException): ApiResult<ErrorResult> {
        val errorMessage = "Request timed out: ${ex.message}"
        logger.warning(errorMessage)
        return ApiResult.error(HttpStatus.GATEWAY_TIMEOUT.value(), errorMessage)

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException): ApiResult<ErrorResult> {
        val errorMessage = "Method not allowed: ${ex.method}"
        logger.warning(errorMessage)
        return ApiResult.error(HttpStatus.METHOD_NOT_ALLOWED.value(), errorMessage)
    }

    @ExceptionHandler(UnsupportedMediaTypeStatusException::class)
    fun handleUnsupportedMediaTypeStatusException(ex: UnsupportedMediaTypeStatusException): ApiResult<ErrorResult> {
        val errorMessage = "Unsupported media type: ${ex.message}"
        logger.warning(errorMessage)
        return ApiResult.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), errorMessage)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ApiResult<ErrorResult> {
        val errorMessage = "Bad Request: ${ex.message}"
        logger.warning(errorMessage)
        return ApiResult.badRequest(errorMessage)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRateLimitException(ex: RuntimeException): ApiResult<ErrorResult> {
        logger.warning(ex.message)
        return ApiResult.error(HttpStatus.TOO_MANY_REQUESTS.value(), ex.message)
    }

}