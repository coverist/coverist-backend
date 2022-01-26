package kw.more.coverist.exception

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ErrorResponse(
    val httpStatus: String,
    val statusCode: Int,
    val message: String?,
    val path: String,
    val timestamp: LocalDateTime
)
