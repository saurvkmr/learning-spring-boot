package com.kumar.learningspringboot.learningspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication(scanBasePackages = {"com.kumar"})
@EnableReactiveMongoRepositories("com.kumar.learningspringboot.repository")
public class LearningSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningSpringBootApplication.class, args);
	}

}
