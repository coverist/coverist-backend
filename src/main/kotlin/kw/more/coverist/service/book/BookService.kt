package kw.more.coverist.service.book

import kw.more.coverist.domain.book.BookRepository
import kw.more.coverist.exception.custom.NonexistentBookException
import kw.more.coverist.web.dto.BookResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BookService {
    @Autowired
    lateinit var bookRepository: BookRepository

    @Transactional(readOnly = true)
    fun findById(id: Long): BookResponseDto {
        return bookRepository.findById(id).orElseThrow { NonexistentBookException() }.toResponseDto()
    }

    @Transactional(readOnly = true)
    fun findByIds(ids: List<Long>): List<BookResponseDto> {
        return bookRepository.findByIds(ids).orElseThrow { NonexistentBookException() }.map { it.toResponseDto() }
    }
}