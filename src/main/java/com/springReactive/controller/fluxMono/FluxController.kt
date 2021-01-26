package com.springReactive.controller.fluxMono

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration

@RestController
@RequestMapping("/flux")
class FluxController {
    @GetMapping
    fun getFlux(): Flux<Int> {
        return Flux.just(1,2,3,4).log()
    }

    @GetMapping("/range")
    fun getFluxWithInterval(): Flux<Int> {
        return Flux.range(0,10)
            .delayElements(Duration.ofSeconds(1))
            .log()
    }

    @GetMapping("/rangeStream", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getFluxWithIntervalStream(): Flux<Long> {
        return Flux.interval(Duration.ofSeconds(1))
            .log()
    }

}