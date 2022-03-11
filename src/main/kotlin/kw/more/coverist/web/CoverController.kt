package kw.more.coverist.web

import kw.more.coverist.service.cover.CoverService
import kw.more.coverist.web.dto.CoverResponseDto
import kw.more.coverist.web.dto.CoverUrlsResponseDto
import kw.more.coverist.web.vo.BookRequestVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class CoverController {
    @Autowired
    lateinit var coverService: CoverService

    @PostMapping("/book")
    fun genNewCover(bookRequestVO: BookRequestVO): List<CoverResponseDto> {
        return coverService.genNewCover(bookRequestVO)
    }

    @PostMapping("/book/{id}")
    fun genBookCover(@PathVariable id: Long): List<CoverResponseDto> {
        return coverService.genBookCover(id)
    }

    @GetMapping("/book/{id}/cover")
    fun getCoversOfBook(@PathVariable id: Long): CoverUrlsResponseDto {
        return coverService.getCoverUrls(id)
    }

    @GetMapping("/cover/{id}")
    fun getCoverById(@PathVariable id: Long): CoverResponseDto {
        return coverService.getCover(id)
    }
}

