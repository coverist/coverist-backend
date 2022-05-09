package kw.more.coverist.service.cover

import com.fasterxml.jackson.databind.ObjectMapper
import kw.more.coverist.domain.book.Book
import kw.more.coverist.domain.book.BookRepository
import kw.more.coverist.domain.book.GENRES
import kw.more.coverist.domain.book.SUB_GENRES
import kw.more.coverist.domain.cover.Cover
import kw.more.coverist.domain.cover.CoverRepository
import kw.more.coverist.exception.custom.*
import kw.more.coverist.util.AmazonS3Util
import kw.more.coverist.web.dto.CoverResponseDto
import kw.more.coverist.web.dto.CoverUrlsResponseDto
import kw.more.coverist.web.dto.GenRequestDto
import kw.more.coverist.web.vo.BookRequestVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate


@Component
class CoverService {
    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var coverRepository: CoverRepository

    @Autowired
    lateinit var amazonS3Util: AmazonS3Util

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var mapper: ObjectMapper

    @Value("\${service.ai-server-host}")
    lateinit var aiServerHost: String

    private final val COVER_GENERATION_COUNT = 4
    private final val DEFAULT_PUBLISHER = "Coverist"

    @Transactional
    fun genNewCover(bookRequestVO: BookRequestVO): List<CoverResponseDto> {
        checkGenreValidation(bookRequestVO)

        if (bookRequestVO.publisher.trim().isBlank())
            bookRequestVO.publisher = DEFAULT_PUBLISHER

        //FIXME: TEST
        return List(4) { idx ->
            CoverResponseDto(
                coverId = 0,
                bookId = 0,
                title = bookRequestVO.title,
                author = bookRequestVO.author,
                genre = bookRequestVO.genre,
                subGenre = bookRequestVO.sub_genre,
                tags = bookRequestVO.tags,
                publisher = bookRequestVO.publisher,
                url = "https://image.yes24.com/goods/89990069/XL",
                createdDate = "2022-01-01T00:00:00"
            )
        }

        val book = bookRepository.save(bookRequestVO.toEntity())

        val bookInfo = bookRequestVO.toBookInfo()
        val genRequestDto = GenRequestDto(bookInfo, COVER_GENERATION_COUNT)

        return genCover(book, genRequestDto)
    }

    @Transactional
    fun genBookCover(id: Long): List<CoverResponseDto> {
        val book = bookRepository.findById(id).orElseThrow { NonexistentBookException() }

        val bookInfo = book.toBookInfo()
        val genRequestDto = GenRequestDto(bookInfo, COVER_GENERATION_COUNT)

        return genCover(book, genRequestDto)
    }

    @Transactional
    fun genCover(book: Book, genRequestDto: GenRequestDto): List<CoverResponseDto> {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(mapper.writeValueAsString(genRequestDto), headers)
        val response: ResponseEntity<List<String>> =
            restTemplate.exchange(
                aiServerHost,
                HttpMethod.POST,
                request,
                object : ParameterizedTypeReference<List<String>>() {}
            )

        if (response.statusCode != HttpStatus.OK)
            throw GenerationFailException(httpStatus = response.statusCode)

        val covers = mutableListOf<CoverResponseDto>()

        for (base64String in response.body!!) {
            val coverUrl = amazonS3Util.uploadFile(base64String, "cover/" + book.id.toString())
            val cover = coverRepository.save(Cover(book = book, url = coverUrl))
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