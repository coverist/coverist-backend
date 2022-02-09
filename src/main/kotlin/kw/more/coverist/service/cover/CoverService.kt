package kw.more.coverist.service.cover

import kw.more.coverist.domain.book.BookRepository
import kw.more.coverist.domain.book.GENRE
import kw.more.coverist.domain.book.SUB_GENRE
import kw.more.coverist.domain.cover.Cover
import kw.more.coverist.domain.cover.CoverRepository
import kw.more.coverist.exception.custom.InvalidGenreException
import kw.more.coverist.exception.custom.InvalidSubGenreException
import kw.more.coverist.exception.custom.NonexistentBookException
import kw.more.coverist.exception.custom.NonexistentCoverException
import kw.more.coverist.web.dto.BookRequestDto
import kw.more.coverist.web.dto.CoverResponseDto
import kw.more.coverist.web.dto.CoverUrlResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CoverService {
    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var coverRepository: CoverRepository

    @Transactional
    fun genNewCover(book: BookRequestDto): CoverResponseDto {
        checkGenreValidation(book)

        val book = bookRepository.save(book.toEntity())

        // 1. book 정보를 AI 서버로 전달 후
        // 2. 생성된 cover 이미지가 AWS S3에 업로드 된 후
        // 3. AI 서버가 본 서버로 S3 URL을 반환한다

        val coverUrl = "COVERIST_COVER_S3_URL_SAMPLE"

        val cover = coverRepository.save(Cover(book = book, url = coverUrl))
        book.covers.add(cover)

        return cover.toResponseDto()
    }

    @Transactional
    fun genBookCover(id: Long): CoverResponseDto {
        val book = bookRepository.findById(id).orElseThrow { NonexistentBookException() }

        // 1. book 정보를 AI 서버로 전달 후
        // 2. 생성된 cover 이미지가 AWS S3에 업로드 된 후
        // 3. AI 서버가 본 서버로 S3 URL을 반환한다

        val coverUrl = "COVERIST_COVER_S3_URL_SAMPLE"

        val cover = coverRepository.save(Cover(book = book, url = coverUrl))
        book.covers.add(cover)

        return cover.toResponseDto()
    }

    @Transactional(readOnly = true)
    fun findByBook(id: Long): CoverUrlResponseDto {
        val covers = bookRepository.findById(id).orElseThrow { NonexistentBookException() }.covers
        return CoverUrlResponseDto(covers.map { it.url })
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): CoverResponseDto {
        return coverRepository.findById(id).orElseThrow { NonexistentCoverException() }.toResponseDto()
    }

    fun checkGenreValidation(book: BookRequestDto) {
        if (book.genre !in GENRE) throw InvalidGenreException()
        if (book.subGenre !in SUB_GENRE[book.genre]!!) throw InvalidSubGenreException()
    }
}