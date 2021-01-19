package funWithFluxMono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoTest {

    @Test
    public void fluxPassTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Spring Reactive").log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Spring Reactive")
                .verifyComplete();
    }

    @Test
    public void fluxErrorTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Spring Reactive")
                .concatWith(Flux.error(new IllegalArgumentException()))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Spring Reactive")
                .verifyError(IllegalArgumentException.class);
    }

    @Test
    public void fluxCompleteTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Spring Reactive")
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot")
                .expectNext("Spring Reactive")
                .verifyComplete();
    }
}
