package kw.more.coverist.web

import kw.more.coverist.service.cover.CoverService
import kw.more.coverist.web.dto.BookRequestDto
import kw.more.coverist.web.dto.CoverResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class CoverController {
    @Autowired
    lateinit var coverService: CoverService

    @PostMapping("/book")
    fun gen(@RequestBody bookRequestDto: BookRequestDto): CoverResponseDto {
        return coverService.genCover(bookRequestDto)
    }
}