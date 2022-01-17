package kw.more.coverist.domain.cover

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kw.more.coverist.domain.BaseTimeEntity
import kw.more.coverist.domain.book.Book
import javax.persistence.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Entity
class Cover (
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       @Column(name = "cover_id")
       var id: Long? = null,

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "book_id")
       var book: Book,

       var url: String

) : BaseTimeEntity()