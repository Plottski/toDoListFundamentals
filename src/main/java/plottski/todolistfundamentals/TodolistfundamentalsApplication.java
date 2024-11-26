package plottski.todolistfundamentals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@Configuration
@EnableJpaRepositories
//@EntityScan("plottski.todolistfundamentals.Entities")
public class TodolistfundamentalsApplication {

	public static void main(String[] args) {

		SpringApplication.run(TodolistfundamentalsApplication.class, args);
	}

}
