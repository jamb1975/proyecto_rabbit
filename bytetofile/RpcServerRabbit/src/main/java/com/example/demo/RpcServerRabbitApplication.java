package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class RpcServerRabbitApplication {

	@Autowired
	RabbitMqAdapter rabbitMqAdapter;
	
	public static void main(String[] args) {
		SpringApplication.run(RpcServerRabbitApplication.class, args);
	}
	

	@Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> {
          
            rabbitMqAdapter.consume();
        };
    }

}
