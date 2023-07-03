package com.example.demo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class RpcClientRabbitApplication {
  
	@Autowired
	private RabbitMqAdapter rabbitMqAdapter;
	
	public static void main(String[] args) {
		SpringApplication.run(RpcClientRabbitApplication.class, args);
	}
	
	
	@Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> {
            Flux.range(1, 700000).log()
            
            .flatMap(num -> {
				
					try {
						return rabbitMqAdapter.sendMessageToSignUp(String.valueOf(num)).log();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				
				
				
			})
                       
            .subscribe();
            
        };
    }
}
