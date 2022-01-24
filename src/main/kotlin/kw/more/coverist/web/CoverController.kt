package kw.more.coverist.web

import kw.more.coverist.service.cover.CoverService
import kw.more.coverist.web.dto.BookRequestDto
import kw.more.coverist.web.dto.CoverResponseDto
import kw.more.coverist.web.dto.CoverUrlResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class CoverController {
    @Autowired
    lateinit var coverService: CoverService

    @PostMapping("/book")
    fun genNewCover(@RequestBody bookRequestDto: BookRequestDto): CoverResponseDto {
        return coverService.genNewCover(bookRequestDto)
    }

    @PostMapping("/book/{id}")
    fun genBookCover(@PathVariable id: Long): CoverResponseDto {
        return coverService.genBookCover(id)
    }

    @GetMapping("/book/{id}/cover")
    fun getCoversOfBook(@PathVariable id: Long): CoverUrlResponseDto {
        return coverService.findByBook(id)
    }

    @GetMapping("/cover/{id}")
    fun getCoverById(@PathVariable id: Long): CoverResponseDto {
        return coverService.findById(id)
    }
}