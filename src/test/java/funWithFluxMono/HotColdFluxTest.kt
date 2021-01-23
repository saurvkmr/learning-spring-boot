package funWithFluxMono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.time.Duration

class HotColdFluxTest {
    @Test
    fun coldFlux() {
        val coldFlux = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
            .delayElements(Duration.ofSeconds(1))
            .log()

        coldFlux.subscribe { println("subscriber 1 - element is $it") }
        Thread.sleep(2000)

        coldFlux.subscribe { println("subscriber 2 - element is $it") }
        Thread.sleep(4000)
    }

    @Test
    fun hotFlux() {
        val hotFlux = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
            .delayElements(Duration.ofSeconds(1))
            //.log()

        val connectableFlux = hotFlux.publish()
        connectableFlux.connect()
        connectableFlux.subscribe { println("subscriber 1 - element is $it") }
        Thread.sleep(2000)

        connectableFlux.subscribe { println("subscriber 2 - element is $it") }
        Thread.sleep(4000)
    }
}