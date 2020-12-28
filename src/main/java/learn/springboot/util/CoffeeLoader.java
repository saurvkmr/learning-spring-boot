package learn.springboot.util;

import learn.springboot.model.Coffee;
import learn.springboot.repository.CoffeeRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class CoffeeLoader {
    private CoffeeRepository coffeeRepository;

    public CoffeeLoader(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @PostConstruct
    private void loadCoffee() {
        this.coffeeRepository.saveAll(Arrays.asList(
                new Coffee("Café Cereza"),
                new Coffee("Café Ganador"),
                new Coffee("Café Lareño"),
                new Coffee("Café Três Pontas")
        ));
    }
}
