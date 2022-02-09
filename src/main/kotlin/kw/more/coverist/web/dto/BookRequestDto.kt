package kw.more.coverist.web.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kw.more.coverist.domain.book.Book

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BookRequestDto(
    val title: String,
    val author: String,
    val genre: String,
    val subGenre: String,
    val tags: Set<String>,
    val publisher: String?
) {
    fun toEntity(): Book {
        return Book(
            title = title,
            author = author,
            genre = genre,
            subGenre = subGenre,
            tags = tags,
            publisher = publisher
        )
    }
}