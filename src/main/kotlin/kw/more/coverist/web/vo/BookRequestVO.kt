package kw.more.coverist.web.vo

import kw.more.coverist.domain.book.Book
import kw.more.coverist.web.dto.BookInfo

data class BookRequestVO(
    val title: String,
    val author: String,
    val genre: String,
    val sub_genre: String,
    val tags: Set<String>,
    var publisher: String
) {
    fun toEntity(): Book {
        return Book(
            title = title,
            author = author,
            genre = genre,
            subGenre = sub_genre,
            tags = tags,
            publisher = publisher
        )
    }

    fun toBookInfo(): BookInfo {
        return BookInfo(
            title = title,
            author = author,
            genre = genre,
            subGenre = sub_genre,
            tags = tags.toList(),
            publisher = publisher
        )
    }
}