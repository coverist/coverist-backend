package kw.more.coverist.exception.custom

import kw.more.coverist.exception.CommonException
import org.springframework.http.HttpStatus

class GenerationFailException(
    message: String = "표지 생성에 실패하였습니다",
    httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : CommonException(message, httpStatus)