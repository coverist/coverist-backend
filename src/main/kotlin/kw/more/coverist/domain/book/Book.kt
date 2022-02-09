package kw.more.coverist.domain.book

import kw.more.coverist.domain.BaseTimeEntity
import kw.more.coverist.domain.cover.Cover
import kw.more.coverist.web.dto.BookResponseDto
import javax.persistence.*

@Entity
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    var id: Long? = null,

    var title: String,

    var author: String,

    var genre: String,

    @Column(name = "sub_genre")
    var subGenre: String,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tag", joinColumns = [JoinColumn(name = "book_id")])
    @Column(name = "text")
    var tags: Set<String>,

    var publisher: String?,

    @OneToMany(mappedBy = "book")
    var covers: MutableList<Cover> = mutableListOf<Cover>()

) : BaseTimeEntity() {
    fun toResponseDto(): BookResponseDto {
        return BookResponseDto(
            id = id,
            title = title,
            author = author,
            genre = genre,
            subGenre = subGenre,
            tags = tags,
            publisher = publisher,
            covers = covers.map { it.id }
        )
    }
}
