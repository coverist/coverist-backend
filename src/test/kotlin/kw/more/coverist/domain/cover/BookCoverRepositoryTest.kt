package kw.more.coverist.domain.cover

import kw.more.coverist.domain.book.Book
import kw.more.coverist.domain.book.BookRepository
import kw.more.coverist.domain.book.Genre
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.assertj.core.api.BDDAssertions.then

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
internal class BookCoverRepositoryTest(val bookRepository: BookRepository, val coverRepository: CoverRepository) {
    @Test
    internal fun `이미지 생성에 따른 표지 생성 테스트`() {
        // given
        val title = "제목"
        val author = "저자"
        val genre = Genre.A
        val tags = setOf("태그1", "태그2", "태그3")
        val publisher = "출판"

        // when
        val book = bookRepository.save(Book(title = title, author = author, genre = genre, tags = tags, publisher = publisher))
        val cover =  coverRepository.save(Cover(book = book, url = "https://image.coverist.io/id/${book.id}"))
        book.covers.add(cover)

        // then
        then(book.title).isEqualTo(title)
        then(book.author).isEqualTo(author)
        then(book.genre).isEqualTo(genre)
        then(book.tags).isEqualTo(tags)
        then(book.publisher).isEqualTo(publisher)

        then(book.createdDate).isNotNull
        then(cover.createdDate).isNotNull

        then(cover.book).isEqualTo(book)
        then(book.covers[0]).isEqualTo(cover)
    }
}