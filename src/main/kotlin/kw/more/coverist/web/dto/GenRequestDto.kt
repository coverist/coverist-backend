package kw.more.coverist.web.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GenRequestDto(
    val bookInfo: BookInfo,
    val count: Int
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BookInfo(
    val title: String,
    val author: String,
    val genre: String,
    val subGenre: String,
    val tags: List<String>,
    val publisher: String?
)