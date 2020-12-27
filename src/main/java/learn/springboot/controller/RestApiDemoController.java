package learn.springboot.controller;

import learn.springboot.model.Coffee;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController("/coffee")
public class RestApiDemoController {
    private List<Coffee> coffees = new ArrayList<>();

    public RestApiDemoController() {
        coffees.addAll(Arrays.asList(
                new Coffee("Café Cereza"),
                new Coffee("Café Ganador"),
                new Coffee("Café Lareño"),
                new Coffee("Café Três Pontas")
        ));
    }

    @GetMapping
    ResponseEntity<Iterable<Coffee>> getCoffee() {
        return new ResponseEntity<>(coffees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<Coffee>> getCoffeeById(@PathVariable String id) {
        return new ResponseEntity<>
                (coffees.stream().filter(coffee -> coffee.getId().equalsIgnoreCase(id)).findFirst(), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Coffee> postCoffee(@RequestBody Coffee coffee) {
        coffees.add(coffee);
        return new ResponseEntity<>(coffee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
        Optional<Coffee> opt = coffees.stream()
                .filter(coffee1 -> coffee1.getId().equalsIgnoreCase(id))
                .findFirst();
        return opt.isPresent()
                ? new ResponseEntity<>(coffee, HttpStatus.OK)
                : postCoffee(coffee);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Boolean> deleteCoffee(@PathVariable String id) {
        return new ResponseEntity<>(coffees.removeIf(c -> c.getId().equalsIgnoreCase(id)), HttpStatus.OK);
    }
}
