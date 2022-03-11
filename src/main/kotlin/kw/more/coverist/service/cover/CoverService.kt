package kw.more.coverist.service.cover

import kw.more.coverist.domain.book.BookRepository
import kw.more.coverist.domain.book.GENRES
import kw.more.coverist.domain.book.SUB_GENRES
import kw.more.coverist.domain.cover.Cover
import kw.more.coverist.domain.cover.CoverRepository
import kw.more.coverist.exception.custom.InvalidGenreException
import kw.more.coverist.exception.custom.InvalidSubGenreException
import kw.more.coverist.exception.custom.NonexistentBookException
import kw.more.coverist.exception.custom.NonexistentCoverException
import kw.more.coverist.util.AmazonS3Util
import kw.more.coverist.web.dto.CoverResponseDto
import kw.more.coverist.web.dto.CoverUrlsResponseDto
import kw.more.coverist.web.vo.BookRequestVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CoverService {
    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var coverRepository: CoverRepository

    @Autowired
    lateinit var amazonS3Util: AmazonS3Util

    @Transactional
    fun genNewCover(bookRequestVO: BookRequestVO): List<CoverResponseDto> {
        checkGenreValidation(bookRequestVO)

        val publisherUrl = bookRequestVO.publisher?.let {
            amazonS3Util.uploadFile(it, "publisher")
        }

        val bookId = bookRepository.save(bookRequestVO.toEntity(publisherUrl)).id

        return genBookCover(bookId!!)
    }

    @Transactional
    fun genBookCover(id: Long): List<CoverResponseDto> {
        val book = bookRepository.findById(id).orElseThrow { NonexistentBookException() }

        // 현재는 테스트를 위해 cover를 book의 publisher 이미지로 대체하여 반환
        // FIXME: 테스트 종료 후 ?: "null" 지울 것
        val coverUrl = List(4) { book.publisher ?: "null" }

        val covers = mutableListOf<CoverResponseDto>()

        for (url in coverUrl) {
            val cover = coverRepository.save(Cover(book = book, url = url))
            book.covers.add(cover)
            covers.add(cover.toResponseDto())
        }

        return covers
    }

    @Transactional(readOnly = true)
    fun getCoverUrls(id: Long): CoverUrlsResponseDto {
        val covers = bookRepository.findById(id).orElseThrow { NonexistentBookException() }.covers
        return CoverUrlsResponseDto(covers.map { it.url })
    }

    @Transactional(readOnly = true)
    fun getCover(id: Long): CoverResponseDto {
        return coverRepository.findById(id).orElseThrow { NonexistentCoverException() }.toResponseDto()
    }

    fun checkGenreValidation(book: BookRequestVO) {
        val genreId = GENRES.find { it.text == book.genre }?.id ?: throw InvalidGenreException()
        SUB_GENRES[genreId]!!.find { it == book.sub_genre } ?: throw InvalidSubGenreException()
    }
}