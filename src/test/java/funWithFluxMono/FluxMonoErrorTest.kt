package funWithFluxMono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.util.retry.Retry
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.time.Duration

class FluxMonoErrorTest {

    @Test
    fun fluxErrorHandling() {
        val concatError = Flux.just("A", "B", "C")
            .concatWith(Flux.error(RuntimeException("Runtime exception")))
            .onErrorContinue { throwable, _ ->  print(throwable)}
            .concatWithValues("D")
            .log()

        StepVerifier.create(concatError)
            .expectSubscription()
            .expectNext("A", "B", "C")
            .expectError(RuntimeException::class.java)
            .verify()
    }

    @Test
    fun fluxContinueOnError() {
        val concatError = Flux.just("A", "B", "C")
            //.concatWith(Flux.error(RuntimeException("Runtime exception")))
            .onErrorContinue { throwable, _ ->  print(throwable); return@onErrorContinue}
            .concatWithValues("D")
            .log()

        StepVerifier.create(concatError)
            .expectSubscription()
            .expectNext("A", "B", "C", "D")
            //.expectError(RuntimeException::class.java)
            .verifyComplete()
    }

    @Test
    fun fluxOnErrorMap() {
        val concatError = Flux.just("A", "B", "C")
            .concatWith(Flux.error(NullPointerException("Runtime exception")))
            .concatWithValues("D")
            .onErrorMap { Exception(it.message) }
            .log()

        StepVerifier.create(concatError)
            .expectSubscription()
            .expectNext("A", "B", "C")
            .expectError(java.lang.Exception::class.java)
            //.expectError(RuntimeException::class.java)
            .verify()
    }

    @Test
    fun fluxOnErrorRetry() {
        val concatError = Flux.just("A", "B", "C")
            .concatWith(Flux.error(NullPointerException("Runtime exception")))
            .concatWithValues("D")
            .onErrorMap { Exception(it.message) }
            .retry(2)
            .log()

        StepVerifier.create(concatError)
            .expectSubscription()
            .expectNext("A", "B", "C")
            .expectNext("A", "B", "C")
            .expectNext("A", "B", "C")
            .expectError(java.lang.Exception::class.java)
            //.expectError(RuntimeException::class.java)
            .verify()
    }
    @Test
    fun fluxOnErrorRetryWithBackOff() {
        val flux = Flux.just("A", "B")
            .concatWith(Flux.error(IllegalStateException("Illegal State")))
            .concatWithValues("D")
            .onErrorMap { Exception(it.message) }
            .retryWhen(Retry.backoff(2, Duration.ofSeconds(2))) // retry works on parallel thread
            //.subscribe()
            //.blockFirst()

        StepVerifier.create(flux.log())
            .expectSubscription()
            .expectNext("A", "B")
            .expectNext("A", "B")
            .expectNext("A", "B")
            .expectError()
            .verify()

    }

    @Test
    fun monoErrorTest() {
        StepVerifier.create(Mono.error<Any>(IllegalStateException("Illegal State")))
            .expectError(IllegalStateException::class.java)
            .verify()
    }
}