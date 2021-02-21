package funWithFluxMono.test

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class FluxMonoInfinite {
    @Test
    fun infiniteSeq() {
        val infiniteFlux = Flux.interval(Duration.ofMillis(10)) // long value starting from 0
            .log()

        infiniteFlux.subscribe { println(it) }

        Thread.sleep(10000)
    }

    @Test
    fun infiniteSeqTest() {
        val infiniteSeq = Flux.interval(Duration.ofSeconds(1))
            .take(5)
            .log()

        StepVerifier.create(infiniteSeq)
            .expectNext(0, 1, 2, 3, 4)
            .verifyComplete()
    }
}