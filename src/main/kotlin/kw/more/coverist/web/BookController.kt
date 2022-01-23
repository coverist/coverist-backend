package kw.more.coverist.web

import kw.more.coverist.service.cover.BookService
import kw.more.coverist.web.dto.BookResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class BookController {
    @Autowired
    lateinit var bookService: BookService

    @GetMapping("/book/{id}")
    fun getBookById(@PathVariable id: Long): BookResponseDto {
        return bookService.findById(id)
    }
}