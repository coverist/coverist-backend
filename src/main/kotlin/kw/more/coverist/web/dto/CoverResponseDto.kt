package kw.more.coverist.web.dto

import kw.more.coverist.domain.book.Genre

data class CoverResponseDto (
        val id: Long?,
        val title: String,
        val author: String,
        val genre: String,
        val tags: Set<String>,
        val publisher: String,
        val url: String
)