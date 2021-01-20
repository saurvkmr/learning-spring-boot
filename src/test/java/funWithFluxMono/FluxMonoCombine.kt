package funWithFluxMono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class FluxMonoCombine {
    @Test
    fun combineFluxTest() {

        val mergeFlux = Flux.merge(Flux.just("A", "B", "C"), Flux.just("E", "F", "G")).log()
        StepVerifier.create(mergeFlux)
            .expectSubscription()
            .expectNext("A", "B", "C", "E", "F", "G")
            .verifyComplete()
    }

    @Test
    fun combineFluxTestDelay() {
        // merge don't maintain order, with delay runs in parallel
        val mergeFlux = Flux.merge(
            Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100)),
            Flux.just("E", "F", "G").delayElements(Duration.ofMillis(100)))
            .delayElements(Duration.ofMillis(100))
            .log()
        StepVerifier.create(mergeFlux)
            .expectSubscription()
            //.expectNext("A", "B", "C", "E", "F", "G")
            .expectNextCount(6)
            .verifyComplete()
    }

    @Test
    fun combineFluxTestDelayConcat() {
        // concat maintains order
        val mergeFlux = Flux.concat(
            Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100)),
            Flux.just("E", "F", "G").delayElements(Duration.ofMillis(100)))
            .delayElements(Duration.ofMillis(100))
            .log()
        StepVerifier.create(mergeFlux)
            .expectSubscription()
            //.expectNext("A", "B", "C", "E", "F", "G")
            .expectNextCount(6)
            .verifyComplete()
    }

    @Test
    fun combineFluxTestDelayZip() {
        // concat maintains order
        val mergeFlux = Flux.zip(
            Flux.just("A", "B", "C"),
            Flux.just("E", "F", "G"))
            .delayElements(Duration.ofMillis(100))
            .log()
        StepVerifier.create(mergeFlux)
            .expectSubscription()
            //.expectNext("A", "B", "C", "E", "F", "G")
            .expectNextCount(3)
            .verifyComplete()
    }

    @Test
    fun combineFluxTestDelayZipTogether() {
        // concat maintains order
        val flux1 = Flux.just("A", "B", "C")
        val flux2 = Flux.just("A", "B", "C")

        val mergeFlux = Flux.zip(flux1, flux2,
            { f1: String, f2: String? -> f1 + f2 })
            .delayElements(Duration.ofMillis(100))
            .log()
        StepVerifier.create(mergeFlux)
            .expectSubscription()
            //.expectNext("A", "B", "C", "E", "F", "G")
            .expectNextCount(3)
            .verifyComplete()
    }
}