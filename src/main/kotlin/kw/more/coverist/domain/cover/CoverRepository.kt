package kw.more.coverist.domain.cover

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CoverRepository : JpaRepository<Cover, Long> {
    @Query("SELECT c FROM cover c INNER JOIN c.book b WHERE b.id = :id", nativeQuery = true)
    fun findByBook(id: Long): List<Cover>
}