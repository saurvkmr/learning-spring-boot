package funWithFluxMono.test

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import reactor.test.scheduler.VirtualTimeScheduler
import java.time.Duration

class VirtualTimeSchedulerTest {
    @Test
    fun virtualTimeSchedulerTest() {
        VirtualTimeScheduler.getOrSet()
        val fluxWithDelay = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .delayElements(Duration.ofSeconds(1))

        StepVerifier.withVirtualTime { fluxWithDelay.log() }
            .expectSubscription()
            .thenAwait(Duration.ofSeconds(9))
            .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .verifyComplete()
    }
}