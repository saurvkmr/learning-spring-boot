package funWithFluxMono.test

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.test

class FluxMonoWithIterable {

    @Test
    fun fluxIterable() {
        val flux = Flux.just("Spring", "Spring Boot")

        flux.test()
            .expectNext("Spring")
            .expectNext("Spring Boot")
            .verifyComplete()

    }

    @Test
    fun monoErrorTest() {
        StepVerifier.create(Mono.error<Any>(IllegalStateException("Illegal State")))
            .expectError(IllegalStateException::class.java)
            .verify()
    }

    @Test
    fun fluxFromIterable() {
        val list = mutableListOf("Berlin", "Dortmund", "Munich")
        StepVerifier.create(Flux.fromIterable(list).log())
            .expectSubscription()
            .assertNext { it.equals("Berlin") }
            .assertNext { it.equals("Dortmund") }
            .assertNext { it.equals("Munich") }
            .verifyComplete()
    }

    @Test
    fun fluxWithRange() {
        StepVerifier.create(Flux.range(1, 100).log())
            .expectNext(1, 2, 3)
            .expectNextCount(99);
    }

    @Test
    fun supplierTest() {
        StepVerifier.create(Mono.fromSupplier { "Spring" })
            .expectNext("Spring")
            .verifyComplete()
    }
}