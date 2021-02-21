package funWithFluxMono.test;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

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
    public void fluxErrorMsgTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Spring Reactive")
                .concatWith(Flux.error(new IllegalArgumentException("Illegal Argument")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Spring Reactive")
                .verifyErrorMessage("Illegal Argument");
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

    @Test
    public void monoTest() {
        Mono<String> stringMono = Mono.justOrEmpty("Spring").log();

        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .expectComplete();
    }

    @Test
    public void monoErrorTest() {
        StepVerifier.create(Mono.error(new IllegalStateException("Illegal State")))
                .expectError(IllegalStateException.class)
                .verify();
    }
}
