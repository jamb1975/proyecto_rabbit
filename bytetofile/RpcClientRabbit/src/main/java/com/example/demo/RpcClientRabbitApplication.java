package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class RpcClientRabbitApplication {
  
	private File f1 = new File("C:\\curssos_faltantes\\backup_2023.zip");
	@Autowired
	private RabbitMqAdapter rabbitMqAdapter;
	
	public static void main(String[] args) {
		SpringApplication.run(RpcClientRabbitApplication.class, args);
	}
	//	6611831 2 1 1 018000940304 1 1 1  medicina general 
	//	6611613 3173253276 odontologia 
	//7856787
	@Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> {
          //  Flux.range(1, 700000).log()
            Mono.just(Files.readAllBytes(f1.toPath()))
        	//Mono.just("file".getBytes())
            .flatMap(fil -> 
				
            	   rabbitMqAdapter.senderBackup(fil)
					/*try {
						//return rabbitMqAdapter.sendMessageToSignUp(String.valueOf(num)).log();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				
				
				
				
			).log()
                       
            .subscribe();
            
        };
    }
}
