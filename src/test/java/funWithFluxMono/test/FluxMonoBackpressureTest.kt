package funWithFluxMono.test

import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class FluxMonoBackpressureTest {
    @Test
    fun backPressureTest() {
        val finiteFlux = Flux.range(1, 10)
            .log()

        StepVerifier.create(finiteFlux)
            .expectSubscription()
            .thenRequest(1)
            .expectNext(1)
            .thenRequest(1)
            .expectNext(2)
            .thenRequest(1)
            .expectNext(3)
            .thenCancel()
            .verify()

    }
}