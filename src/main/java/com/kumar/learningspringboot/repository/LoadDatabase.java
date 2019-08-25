package com.kumar.learningspringboot.repository;

import com.kumar.learningspringboot.vo.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class LoadDatabase {

    @Autowired
    ChapterRepository repository;

    @Bean
    @SuppressWarnings("all")
    CommandLineRunner init() {
        System.out.println("i ran");
        return args -> {
            Flux.just(
                    new Chapter("1001","Quick Start with Java"),
                    new Chapter("1002","Reactive Web with Spring Boot"),
                    new Chapter("1003","...and more!"))
                    .flatMap(repository::save)
                    .subscribe(System.out::println);
        };
    }
}
