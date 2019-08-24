package com.kumar.learningspringboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/greet")
    public String greeting(@RequestParam(required = false, defaultValue = "") String name) {
        return name.equalsIgnoreCase("") ? "Hey" : String.format("Hey %s!", name);
    }
}
