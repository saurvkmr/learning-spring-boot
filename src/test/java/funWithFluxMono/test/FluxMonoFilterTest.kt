package funWithFluxMono.test

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class FluxMonoFilterTest {
    @Test
    fun testFilterFromList() {
        StepVerifier.create(
            Flux.fromIterable(mutableListOf("Spring", "Berlin", "Spring Boot", "Singapore"))
                .filter { it.contains("Spring") }
                .log())
            .expectSubscription()
            .expectNext("Spring", "Spring Boot")
            .verifyComplete()
    }
}