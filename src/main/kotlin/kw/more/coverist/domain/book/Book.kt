package kw.more.coverist.domain.book

import kw.more.coverist.domain.BaseTimeEntity
import kw.more.coverist.domain.cover.Cover
import javax.persistence.*

@Entity
class Book  (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    var id: Long? = null,

    var title: String,

    var author: String,

    @Enumerated(EnumType.STRING)
    var genre: Genre,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tag", joinColumns = [JoinColumn(name = "book_id")])
    @Column(name = "text")
    var tags: Set<String>,

    var publisher: String,

    @OneToMany(mappedBy = "book")
    var covers: MutableList<Cover> = mutableListOf<Cover>()

) : BaseTimeEntity()
