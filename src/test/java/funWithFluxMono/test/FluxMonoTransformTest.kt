package funWithFluxMono.test

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class FluxMonoTransformTest {
    val arr = mutableListOf("Spring")

    @Test
    fun fluxTransformUsingMap() {


        StepVerifier.create(
            Flux.fromIterable(arr)
                .map { it.toUpperCase() }
                .log())
            .expectNext("SPRING")
            .verifyComplete()
    }

    @Test
    fun fluxTransformToLen() {
        StepVerifier.create(
            Flux.fromIterable(arr)
                .map { it.length }
                .repeat(1)
                .log())
            .expectNext(6)
            .expectNext(6)
            .verifyComplete()
    }

    @Test
    fun transformUsingFlaxMap() {
        val arr = Flux.fromIterable(mutableListOf("A", "B", "C","D", "E", "F",))
            .window(2)
            //.flatMap { convertToList(it). }
            .log()

        StepVerifier.create(arr)
            .expectNextCount(3)
            .verifyComplete();


    }

    private fun convertToList(s: String): MutableList<String> {
        Thread.sleep(1000)
        return mutableListOf(s, " New Value Added")
    }
}