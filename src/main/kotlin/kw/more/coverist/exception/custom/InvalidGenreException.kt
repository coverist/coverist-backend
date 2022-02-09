package kw.more.coverist.exception.custom

import kw.more.coverist.exception.CommonException
import org.springframework.http.HttpStatus

class InvalidGenreException(
    message: String = "유효하지 않은 Genre 입니다.",
    httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : CommonException(message, httpStatus)