package learn.springboot.controller;

import learn.springboot.model.Coffee;
import learn.springboot.repository.CoffeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@RestController("/coffee")
public class RestApiDemoController {
    CoffeeRepository coffeeRepository;

    public RestApiDemoController(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
        this.coffeeRepository.saveAll(Arrays.asList(
                new Coffee("Café Cereza"),
                new Coffee("Café Ganador"),
                new Coffee("Café Lareño"),
                new Coffee("Café Três Pontas")
        ));
    }

    @GetMapping
    ResponseEntity<Iterable<Coffee>> getCoffee() {
        return new ResponseEntity<>(coffeeRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<Coffee>> getCoffeeById(@PathVariable String id) {
        return new ResponseEntity<>(this.coffeeRepository.findById(id), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Coffee> postCoffee(@RequestBody Coffee coffee) {
        return new ResponseEntity<>(this.coffeeRepository.save(coffee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
        return coffeeRepository.existsById(id)
                ? new ResponseEntity<>(coffee, HttpStatus.OK)
                : postCoffee(coffee);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCoffee(@PathVariable String id) {
        coffeeRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
