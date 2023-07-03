package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReadRabbitMqApplication {
@Autowired
private RabbitMqAdapter rabbitMqAdapter;
	public static void main(String[] args) {
		SpringApplication.run(ReadRabbitMqApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner() {
		rabbitMqAdapter.readRAbbitMq();
		
		return null;
		
	}

}
