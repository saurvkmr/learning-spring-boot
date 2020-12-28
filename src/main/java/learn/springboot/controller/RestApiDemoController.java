package learn.springboot.controller;

import learn.springboot.model.Coffee;
import learn.springboot.repository.CoffeeRepository;
import learn.springboot.util.CoffeeLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/coffee")
public class RestApiDemoController {
    CoffeeRepository coffeeRepository;
    CoffeeLoader coffeeLoader;

    @Autowired
    public RestApiDemoController(CoffeeRepository coffeeRepository, CoffeeLoader coffeeLoader) {
        this.coffeeRepository = coffeeRepository;
        this.coffeeLoader = coffeeLoader;
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
