package kw.more.coverist.web.dto

import kw.more.coverist.domain.book.Book
import kw.more.coverist.domain.book.Genre

data class BookRequestDto (
    val title: String,
    val author: String,
    val genre: String,
    val tags: Set<String>,
    val publisher: String
) {
    fun toEntity(): Book {
        return Book(
            title = title,
            author = author,
            genre = Genre.valueOf(genre),
            tags = tags,
            publisher = publisher)
    }
}