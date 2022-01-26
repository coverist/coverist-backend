package kw.more.coverist.exception

import org.springframework.http.HttpStatus

open class CommonException(
    override val message: String,
    val httpStatus: HttpStatus
) : RuntimeException(message)