package funWithFluxMono.test;

import reactor.core.publisher.Flux;

public class Test {

    public void zipTogether() {
        Flux<Integer> finiteFlux = Flux.range(0, 10).log();

        finiteFlux.subscribe( element -> System.out.println("element is " + element),
                error -> System.err.println("error occurred " + error),
                () -> System.out.println("Done"),
                subscription -> subscription.request(2)
        );
    }
}
