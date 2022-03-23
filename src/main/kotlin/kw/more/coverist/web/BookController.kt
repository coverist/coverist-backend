package kw.more.coverist.web

import kw.more.coverist.domain.book.GENRES
import kw.more.coverist.domain.book.Genre
import kw.more.coverist.domain.book.SUB_GENRES
import kw.more.coverist.exception.custom.InvalidGenreException
import kw.more.coverist.service.book.BookService
import kw.more.coverist.web.dto.BookResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class BookController {
    @Autowired
    lateinit var bookService: BookService

    @GetMapping("/book")
    fun getBookByIds(@RequestParam id: String): List<BookResponseDto> {
        val ids = id.split(",").map { it.toLong() }
        return bookService.findByIds(ids)
    }

    @GetMapping("/book/genre")
    fun getBookGenre(): List<Genre> {
        return GENRES
    }

    @GetMapping("/book/genre/{genre_id}/subgenre")
    fun getBookSubGenre(@PathVariable("genre_id") genreId: Int): List<Genre> {
        val genre = GENRES.find { it.id == genreId } ?: throw InvalidGenreException()
        return SUB_GENRES[genre.id]!!.map { Genre(0, it) }
    }
}