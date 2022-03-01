package kw.more.coverist.web

import kw.more.coverist.domain.book.GENRES
import kw.more.coverist.domain.book.Genre
import kw.more.coverist.domain.book.SUB_GENRES
import kw.more.coverist.exception.custom.InvalidGenreException
import kw.more.coverist.service.book.BookService
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

    @GetMapping("/book/genre")
    fun getBookGenre(): List<Genre> {
        return GENRES
    }

    @GetMapping("/book/genre/{genre_id}/subgenre")
    fun getBookSubGenre(@PathVariable("genre_id") genreId: Int): List<String> {
        val genre = GENRES.find { it.id == genreId } ?: throw InvalidGenreException()
        return SUB_GENRES[genre.id]!!
    }
}