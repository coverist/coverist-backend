package kw.more.coverist.service.cover

import kw.more.coverist.domain.book.BookRepository
import kw.more.coverist.domain.cover.Cover
import kw.more.coverist.domain.cover.CoverRepository
import kw.more.coverist.web.dto.BookRequestDto
import kw.more.coverist.web.dto.CoverResponseDto
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
    fun genCover(book: BookRequestDto) : CoverResponseDto {
        val book = bookRepository.save(book.toEntity())

        // 1. book 정보를 AI 서버로 전달 후
        // 2. 생성된 cover 이미지가 AWS S3에 업로드 된 후
        // 3. AI 서버가 본 서버로 S3 URL을 반환한다

        val coverUrl = "COVERIST_COVER_S3_URL_SAMPLE"

        val cover = coverRepository.save(Cover(book = book, url = coverUrl))
        book.covers.add(cover)

        return cover.toResponseDto()
    }
}