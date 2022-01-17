package kw.more.coverist.domain.cover

import org.springframework.data.jpa.repository.JpaRepository

interface CoverRepository  : JpaRepository<Cover, Long> {
}