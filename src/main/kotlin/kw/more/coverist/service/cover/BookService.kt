package kw.more.coverist.service.cover

import kw.more.coverist.domain.book.BookRepository
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
        return bookRepository.findById(id).get().toResponseDto()
    }
}