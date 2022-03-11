package kw.more.coverist.web.vo

import kw.more.coverist.domain.book.Book
import org.springframework.web.multipart.MultipartFile

class BookRequestVO(
    val title: String,
    val author: String,
    val genre: String,
    val sub_genre: String,
    val tags: Set<String>,
    val publisher: MultipartFile?
) {
    fun toEntity(publisherUrl: String?): Book {
        return Book(
            title = title,
            author = author,
            genre = genre,
            subGenre = sub_genre,
            tags = tags,
            publisher = publisherUrl
        )
    }
}