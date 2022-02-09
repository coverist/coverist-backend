package kw.more.coverist.exception.custom

import kw.more.coverist.exception.CommonException
import org.springframework.http.HttpStatus

class NonexistentBookException(
    message: String = "존재하지 않는 Book 입니다.",
    httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : CommonException(message, httpStatus)