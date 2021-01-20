package funWithFluxMono;

import reactor.core.publisher.Flux;

import java.util.function.Supplier;

public class Test {

    public void zipTogether() {
        Flux<String> flux1 = Flux.just("A");
        Flux<String> flux2 = Flux.just("A");

        Flux.zip(flux1, flux2, (f1, f2) -> f1.concat(f1));
    }
}
