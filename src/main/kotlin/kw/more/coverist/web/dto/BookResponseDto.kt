package kw.more.coverist.web.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BookResponseDto(
    val id: Long?,
    val title: String,
    val author: String,
    val genre: String,
    val subGenre: String,
    val tags: Set<String>,
    val publisher: String?,
    val coverUrls: List<String>,
    val createdDate: String
)