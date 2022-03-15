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
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*


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

    final val COVER_GENERATION_COUNT = 4

    @Transactional
    fun genNewCover(bookRequestVO: BookRequestVO): List<CoverResponseDto> {
        checkGenreValidation(bookRequestVO)

        var publisherBase64: String? = null
        var publisherUrl: String? = null

        // If publisher image exists, upload it to AWS S3 and encode to base64 string.
        bookRequestVO.publisher?.let {
            publisherUrl = amazonS3Util.uploadFile(it, "publisher")

            val encoder = Base64.getEncoder()
            val encodedImage = encoder.encode(it.bytes)
            publisherBase64 = String(encodedImage, Charsets.UTF_8)
        }

        val book = bookRepository.save(bookRequestVO.toEntity(publisherUrl))

        val bookInfo = bookRequestVO.toBookInfo(publisherBase64)
        val genRequestDto = GenRequestDto(bookInfo, COVER_GENERATION_COUNT)

        return genCover(book, genRequestDto)
    }

    @Transactional
    fun genBookCover(id: Long): List<CoverResponseDto> {
        val book = bookRepository.findById(id).orElseThrow { NonexistentBookException() }

        // If publisher image URL exists, download it from URL and encode to base64 string.
        val tempFilePath = "temp_publisher_image"
        val publisherBase64String = book.publisher?.let {
            val inputStream = BufferedInputStream(URL(it).openStream())
            val outputStream = FileOutputStream(tempFilePath)

            val dataBuffer = ByteArray(1024)
            var bytesRead: Int
            while (true) {
                bytesRead = inputStream.read(dataBuffer, 0, 1024)
                if (bytesRead == -1) break
                outputStream.write(dataBuffer, 0, bytesRead)
            }

            val tempFile = File(tempFilePath)
            val base64 = Base64.getEncoder().encode(tempFile.readBytes())
            tempFile.delete()

            String(base64, Charsets.UTF_8)
        }

        val bookInfo = book.toBookInfo(publisherBase64String)
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