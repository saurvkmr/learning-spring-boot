package com.kumar.learningspringboot.controller

import com.kumar.learningspringboot.repository.ChapterRepository
import com.kumar.learningspringboot.vo.Chapter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class ChapterController(val repository: ChapterRepository) {

    @GetMapping("/chapters")
    fun listing(): Flux<Chapter> {
        return repository.findAll()
    }

}