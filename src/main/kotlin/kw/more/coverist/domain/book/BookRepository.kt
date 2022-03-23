package kw.more.coverist.domain.book

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface BookRepository : JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.id IN :ids")
    fun findByIds(ids: List<Long>): Optional<List<Book>>
}