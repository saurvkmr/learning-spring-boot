package com.springReactive.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/mono")
class MonoController {
    @GetMapping
    fun getMono(): Mono<String> {
        return Mono.just("A").log()
    }
}