package kw.more.coverist.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalControllerAdvice {
    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun httpMessageNotReadableExceptionHandler(
        e: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val message = e.message

        val errorResponse = ErrorResponse(
            httpStatus = httpStatus.toString(),
            statusCode = httpStatus.value(),
            message = message,
            path = request.requestURI.toString(),
            timestamp = LocalDateTime.now()
        )

        return ResponseEntity.status(httpStatus).body(errorResponse)
    }

    @ExceptionHandler(value = [CommonException::class])
    fun commonExceptionHandler(
        e: CommonException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val httpStatus = e.httpStatus
        val message = e.message

        val errorResponse = ErrorResponse(
            httpStatus = httpStatus.toString(),
            statusCode = httpStatus.value(),
            message = message,
            path = request.requestURI.toString(),
            timestamp = LocalDateTime.now()
        )

        return ResponseEntity.status(httpStatus).body(errorResponse)
    }
}