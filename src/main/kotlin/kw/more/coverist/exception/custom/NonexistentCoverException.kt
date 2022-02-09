package kw.more.coverist.exception.custom

import kw.more.coverist.exception.CommonException
import org.springframework.http.HttpStatus

class NonexistentCoverException(
    message: String = "존재하지 않는 Cover 입니다.",
    httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : CommonException(message, httpStatus)