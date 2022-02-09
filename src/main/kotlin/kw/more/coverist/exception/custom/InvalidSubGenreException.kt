package kw.more.coverist.exception.custom

import kw.more.coverist.exception.CommonException
import org.springframework.http.HttpStatus

class InvalidSubGenreException(
    message: String = "유효하지 않은 Sub genre 입니다.",
    httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : CommonException(message, httpStatus)