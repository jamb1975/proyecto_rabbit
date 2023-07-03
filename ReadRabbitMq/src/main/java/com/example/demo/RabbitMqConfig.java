package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

@Configuration
@Data
public class RabbitMqConfig {

	@Autowired
	private RabbitProperties rabbitProperties;
	@Bean
	@Primary
	public Mono<Connection> connMono(){
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(rabbitProperties.getHost());
		connectionFactory.setPort(rabbitProperties.getPort());
		connectionFactory.setUsername(rabbitProperties.getUsername());
		connectionFactory.setPassword(rabbitProperties.getPassword());
		connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
		connectionFactory.useNio();
		return Mono.fromCallable(()-> connectionFactory.newConnection()).cache();
		
	}
	
	@Bean
	public Sender sender(Mono<Connection> connMono) {
		
		return RabbitFlux.createSender(new SenderOptions().connectionMono(connMono));
	}
	
	@Bean
	public Receiver receiver(Mono<Connection> connMono) {
		
		return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connMono));
	}
	
}
